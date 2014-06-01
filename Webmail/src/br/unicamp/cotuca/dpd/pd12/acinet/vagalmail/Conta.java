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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author José Carlos
 */
@Entity
@Table(name = "conta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conta.findAll", query = "SELECT c FROM Conta c"),
    @NamedQuery(name = "Conta.findById", query = "SELECT c FROM Conta c WHERE c.id = :id"),
    @NamedQuery(name = "Conta.findByEmail", query = "SELECT c FROM Conta c WHERE c.email = :email"),
    @NamedQuery(name = "Conta.findByImapHost", query = "SELECT c FROM Conta c WHERE c.imapHost = :imapHost"),
    @NamedQuery(name = "Conta.findByImapPort", query = "SELECT c FROM Conta c WHERE c.imapPort = :imapPort"),
    @NamedQuery(name = "Conta.findByImapUser", query = "SELECT c FROM Conta c WHERE c.imapUser = :imapUser"),
    @NamedQuery(name = "Conta.findByImapPassword", query = "SELECT c FROM Conta c WHERE c.imapPassword = :imapPassword"),
    @NamedQuery(name = "Conta.findBySmtpHost", query = "SELECT c FROM Conta c WHERE c.smtpHost = :smtpHost"),
    @NamedQuery(name = "Conta.findBySmtpPort", query = "SELECT c FROM Conta c WHERE c.smtpPort = :smtpPort"),
    @NamedQuery(name = "Conta.findBySmtpUser", query = "SELECT c FROM Conta c WHERE c.smtpUser = :smtpUser"),
    @NamedQuery(name = "Conta.findBySmtpPassword", query = "SELECT c FROM Conta c WHERE c.smtpPassword = :smtpPassword")})
public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator="sqlite_Conta")
    @TableGenerator(name="sqlite_Conta", table="sqlite_sequence",
            pkColumnName="name", valueColumnName="seq",
            pkColumnValue="conta", allocationSize = 1)
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
    @Column(name = "imap_host")
    private String imapHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "imap_port")
    private int imapPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "imap_user")
    private String imapUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "imap_password")
    private String imapPassword;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "smtp_host")
    private String smtpHost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "smtp_port")
    private int smtpPort;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "smtp_user")
    private String smtpUser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000000000)
    @Column(name = "smtp_password")
    private String smtpPassword;
    @JoinColumn(name = "id_login", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Login idLogin;

    public Conta() {
    }

    public Conta(Integer id) {
        this.id = id;
    }

    public Conta(Integer id, String email, String imapHost, int imapPort, String imapUser, String imapPassword, String smtpHost, int smtpPort, String smtpUser, String smtpPassword) {
        this.id = id;
        this.email = email;
        this.imapHost = imapHost;
        this.imapPort = imapPort;
        this.imapUser = imapUser;
        this.imapPassword = imapPassword;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUser = smtpUser;
        this.smtpPassword = smtpPassword;
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

    public String getImapHost() {
        return imapHost;
    }

    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    public int getImapPort() {
        return imapPort;
    }

    public void setImapPort(int imapPort) {
        this.imapPort = imapPort;
    }

    public String getImapUser() {
        return imapUser;
    }

    public void setImapUser(String imapUser) {
        this.imapUser = imapUser;
    }

    public String getImapPassword() {
        return imapPassword;
    }

    public void setImapPassword(String imapPassword) {
        this.imapPassword = imapPassword;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
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
