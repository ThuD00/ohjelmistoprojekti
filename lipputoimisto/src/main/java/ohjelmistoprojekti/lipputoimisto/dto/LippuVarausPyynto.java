package ohjelmistoprojekti.lipputoimisto.dto;

import java.util.List;

public class LippuVarausPyynto {

    private long tapahtumaId;
    private List<LippuVaraus> liput;

    public LippuVarausPyynto() {
    }

    public long getTapahtumaId() {
        return tapahtumaId;
    }

    public void setTapahtumaId(long tapahtumaId) {
        this.tapahtumaId = tapahtumaId;
    }

    public List<LippuVaraus> getLiput() {
        return liput;
    }

    public void setLiput(List<LippuVaraus> liput) {
        this.liput = liput;
    }
}