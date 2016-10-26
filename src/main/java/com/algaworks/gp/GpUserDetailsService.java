package com.algaworks.gp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class GpUserDetailsService implements UserDetailsService {

private static final Logger logger = Logger.getLogger(GpUserDetailsService.class.getSimpleName());
	
	@Autowired
	private DataSource dataSource;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Connection connection = null;
		
		try {
			connection = dataSource.getConnection();
			
			PreparedStatement ps = connection.prepareStatement("select nome, login, senha, ativo from usuario where login = ?");
			ps.setString(1, username);
			
			ResultSet rs = ps.executeQuery();
			
			if (!rs.next()) {
				throw new UsernameNotFoundException("Usuário " + username + " não encontrado!");
			}
			
			String nome = rs.getString("nome");
			String login = rs.getString("login");
			String password = rs.getString("senha");
			boolean ativo = rs.getBoolean("ativo");
			Collection<GrantedAuthority> permissoes = new ArrayList<>();
			
			rs.close();
			ps.close();
			
			ps = connection.prepareStatement("select u.login, up.permissao as permissao from usuario_permissao up join usuario u on u.id = up.usuario_id where u.login = ?");
			ps.setString(1, username);			
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				permissoes.add(new SimpleGrantedAuthority(rs.getString("permissao")));
			}
			
			rs.close();
			ps.close();
			
			return new GpUserDetails(nome, login, password, ativo, permissoes);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Problemas com a tentativa de conexão!", e);
			throw new UsernameNotFoundException("Problemas com a tentativa de conexão!", e);
		} finally {
			try {
				if (connection != null) {					
					connection.close();
				}
			} catch (SQLException e) {
				logger.log(Level.SEVERE, "Problemas ao tentar fechar a conexão!", e);
				throw new UsernameNotFoundException("Problemas ao tentar fechar a conexão!", e);
			}
		}
	}
	
	public static class GpUserDetails implements UserDetails {
		
		private static final long serialVersionUID = 1L;
		
		private String nome;
		private String login;
		private String password;
		private boolean ativo;
		private Collection<? extends GrantedAuthority> permissoes;
		
		public GpUserDetails(String nome, String login, String password, boolean ativo,
				Collection<? extends GrantedAuthority> permissoes) {
			super();
			this.nome = nome;
			this.login = login;
			this.password = password;
			this.ativo = ativo;
			this.permissoes = permissoes;
		}

		public String getNome() {
			return nome;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return permissoes;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getUsername() {
			return login;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return ativo;
		}
		
	}

}
