package com.hse.cs.ce.LiveExhibition.Repositories;

import com.hse.cs.ce.LiveExhibition.Models.Exhibitors;
import com.hse.cs.ce.LiveExhibition.Models.ExhibitorsGoods;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExhibitorsGoodsRepository extends CrudRepository<ExhibitorsGoods, Long> {
    List<ExhibitorsGoods> findByExhibitorId(Long exhibitorId);

}

