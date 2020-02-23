package com.lavector.collector.entity.wechatSmall.brand;

import com.lavector.collector.entity.BaseAuditableSqlEntity;
import com.lavector.collector.entity.category.Category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

/**
 * Created on 21/12/2017.
 *
 * @author seveniu
 */
@Entity
public class Brand extends BaseAuditableSqlEntity {
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn
    private Category category;
    private String logo;
    private String introduction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(id, brand.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
