package pt.teixeiram2.UrlShortner.model;


import jakarta.persistence.*;

import java.math.BigInteger;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name= "url_mappings")
public class UrlMappingEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "short_url")
    private String shortUrl;
    @Column(name = "url")
    private String url;
    @Column(name = "checksum")
    private long checksum;
    @Column(name = "insert_ts")
    private Date insertTs;
    @Column(name = "version")
    private int version = 1;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getChecksum() {
        return checksum;
    }

    public void setChecksum(long checksum) {
        this.checksum = checksum;
    }

    public Date getInsertTs() {
        return insertTs;
    }

    public void setInsertTs(Date insertTs) {
        this.insertTs = insertTs;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlMappingEntity that = (UrlMappingEntity) o;
        return checksum == that.checksum && version == that.version && Objects.equals(id, that.id) && Objects.equals(shortUrl, that.shortUrl) && Objects.equals(url, that.url) && Objects.equals(insertTs, that.insertTs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortUrl, url, checksum, insertTs, version);
    }

    @Override
    public String toString() {
        return "UrlMappingEntity{" + "id=" + id +
                ", shortUrl='" + shortUrl + '\'' +
                ", url='" + url + '\'' +
                ", checksum=" + checksum +
                ", insertTs=" + insertTs +
                ", version=" + version +
                '}';
    }
}
