package pt.teixeiram2.UrlShortner.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "redirect_service_domain")
public class RedirectServiceDomainEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "domain")
    private String domain;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedirectServiceDomainEntity that = (RedirectServiceDomainEntity) o;
        return id == that.id && Objects.equals(domain, that.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domain);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RedirectServiceDomainEntity.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("domain='" + domain + "'")
                .toString();
    }
}
