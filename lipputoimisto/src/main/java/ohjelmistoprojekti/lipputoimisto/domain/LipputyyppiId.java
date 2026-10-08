package ohjelmistoprojekti.lipputoimisto.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class LipputyyppiId implements Serializable {
    private long tapahtumaId;
    private long lipputyyppiId;

    public LipputyyppiId() {}

    public LipputyyppiId(long tapahtumaId, long lipputyyppiId) {
        this.tapahtumaId = tapahtumaId;
        this.lipputyyppiId = lipputyyppiId;
    }

    public long getTapahtumaId() {
        return tapahtumaId;
    }

    public void setTapahtumaId(long tapahtumaId) {
        this.tapahtumaId = tapahtumaId;
    }

    public long getLipputyyppiId() {
        return lipputyyppiId;
    }

    public void setLipputyyppiId(long lipputyyppiId) {
        this.lipputyyppiId = lipputyyppiId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (tapahtumaId ^ (tapahtumaId >>> 32));
        result = prime * result + (int) (lipputyyppiId ^ (lipputyyppiId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LipputyyppiId other = (LipputyyppiId) obj;
        if (tapahtumaId != other.tapahtumaId)
            return false;
        if (lipputyyppiId != other.lipputyyppiId)
            return false;
        return true;
    }
}
