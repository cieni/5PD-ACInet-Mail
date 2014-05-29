/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unicamp.cotuca.dpd.pd12.acinet.vagalmail;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author u12177
 */
@Entity
@Table(name = "conta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conta.findAll", query = "SELECT c FROM Conta c"),
    @NamedQuery(name = "Conta.findById", query = "SELECT c FROM Conta c WHERE c.id = :id"),
    @NamedQuery(name = "Conta.findByEmail", query = "SELECT c FROM Conta c WHERE c.email = :email"),
    @NamedQuery(name = "Conta.findBySvHost", query = "SELECT c FROM Conta c WHERE c.svHost = :svHost"),
    @NamedQuery(name = "Conta.findBySvPort", query = "SELECT c FROM Conta c WHERE c.svPort = :svPort"),
    @NamedQuery(name = "Conta.findBySvUser", query = "SELECT c FROM Conta c WHERE c.svUser = :svUser"),
    @NamedQuery(name = "Conta.findBySvPassword", query = "SELECT c FROM Conta c WHERE c.svPassword = :svPassword"),
    @NamedQuery(name = "Conta.findBySvType", query = "SELECT c FROM Conta c WHERE c.svType = :svType")})
public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "sv_host")
    private String svHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sv_port")
    private int svPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "sv_user")
    private String svUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "sv_password")
    private String svPassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "sv_type")
    private String svType;
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Login idLogin;

    public Conta() {
    }

    public Conta(Integer id) {
        this.id = id;
    }

    public Conta(Integer id, String email, String svHost, int svPort, String svUser, String svPassword, String svType) {
        this.id = id;
        this.email = email;
        this.svHost = svHost;
        this.svPort = svPort;
        this.svUser = svUser;
        this.svPassword = svPassword;
        this.svType = svType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSvHost() {
        return svHost;
    }

    public void setSvHost(String svHost) {
        this.svHost = svHost;
    }

    public int getSvPort() {
        return svPort;
    }

    public void setSvPort(int svPort) {
        this.svPort = svPort;
    }

    public String getSvUser() {
        return svUser;
    }

    public void setSvUser(String svUser) {
        this.svUser = svUser;
    }

    public String getSvPassword() {
        return svPassword;
    }

    public void setSvPassword(String svPassword) {
        this.svPassword = svPassword;
    }

    public String getSvType() {
        return svType;
    }

    public void setSvType(String svType) {
        this.svType = svType;
    }

    public Login getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(Login idLogin) {
        this.idLogin = idLogin;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conta)) {
            return false;
        }
        Conta other = (Conta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.unicamp.cotuca.dpd.pd12.acinet.vagalmail.Conta[ id=" + id + " ]";
    }
    
}
