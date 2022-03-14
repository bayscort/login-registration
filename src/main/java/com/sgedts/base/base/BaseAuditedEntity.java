package com.sgedts.base.base;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Base class for all audited entity.
 */
@MappedSuperclass
public class BaseAuditedEntity extends BaseEntity {
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createdBy", createdBy)
                .add("modifiedBy", modifiedBy)
                .toString();
    }
}
