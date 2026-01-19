package it.unicam.filiera.controllers.dto;

import java.util.List;

public class LottoTimelineResponse {

    private LottoResponse lotto;
    private List<TimelineItemResponse> timeline;

    public LottoResponse getLotto() { return lotto; }
    public void setLotto(LottoResponse lotto) { this.lotto = lotto; }

    public List<TimelineItemResponse> getTimeline() { return timeline; }
    public void setTimeline(List<TimelineItemResponse> timeline) { this.timeline = timeline; }
}
