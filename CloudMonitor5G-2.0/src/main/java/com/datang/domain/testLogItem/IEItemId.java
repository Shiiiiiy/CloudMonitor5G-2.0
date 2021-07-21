package com.datang.domain.testLogItem;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class IEItemId implements Serializable {
    private long logId;
    private Long timeCol;

    public Long getTimeCol() {
        return timeCol;
    }

    public void setTimeCol(Long timeCol) {
        this.timeCol = timeCol;
    }
    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IEItemId ieItemId = (IEItemId) o;
        return logId == ieItemId.logId &&
                Objects.equals(timeCol, ieItemId.timeCol);
    }

    @Override
    public int hashCode() {

        return Objects.hash(logId, timeCol);
    }
}
