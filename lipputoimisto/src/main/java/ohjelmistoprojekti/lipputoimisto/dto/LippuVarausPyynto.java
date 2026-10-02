package ohjelmistoprojekti.lipputoimisto.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class LippuVarausPyynto {

    @Positive 
    private long tapahtumaId;

    @NotEmpty
    @Valid
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