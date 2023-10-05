package guru.sfg.brewery.domain.security;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import guru.sfg.brewery.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User implements UserDetails, CredentialsContainer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private String username;
	private String password;
	
	@Singular
//	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
	@ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
			joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
	private Set<Role> roles;
	
	@ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
	
//	@Singular
//	@ManyToMany(cascade = CascadeType.MERGE)
//	@JoinTable(name = "user_authority",
//			joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
//			inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
//	@Transient
//	private Set<Authority> authorities;

	@Transient
	public Set<GrantedAuthority> getAuthorities() {
//		return authorities;
		return this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
	}
	
	@Builder.Default
	private Boolean accountNonExpired = true;
	
	@Builder.Default 
	private Boolean accountNonLocked = true;
	
	@Builder.Default 
	private Boolean credentialsNonExpired = true;
	
	@Builder.Default 
	private Boolean enabled = true;

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}	
}
