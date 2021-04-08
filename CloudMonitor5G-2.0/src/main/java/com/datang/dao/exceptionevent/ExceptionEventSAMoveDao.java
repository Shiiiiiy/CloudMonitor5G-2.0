package com.datang.dao.exceptionevent;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAMoveDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 8:56
 */
@Repository
public class ExceptionEventSAMoveDao extends GenericHibernateDao<Iads5gEmbbGateConfig,Long> {
    @Autowired
    private Iads5gEmbbGateConfig gateConfig;
    public void updateDataOfExceptionEventSAMoveparams(ExceptionEventASAMoveDto moveDto) {
        /*BeanUtils.copyProperties(moveDto,gateConfig);
        gateConfig.setCreateTime(new Date());
        this.create(gateConfig);*/
     	Iads5gEmbbGateConfig find = new Iads5gEmbbGateConfig();
    	Iads5gEmbbGateConfig insertRlt = new Iads5gEmbbGateConfig();
    	Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)1));
		find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		if(find==null || find.getGateType()!=1){
			insertRlt.setGateType(moveDto.getGateType());
			insertRlt.setDescription(moveDto.getDescription());
			insertRlt.setTimeHighSaMove(moveDto.getTimeHighSaMove());
			insertRlt.setTimeLowSaMove(moveDto.getTimeLowSaMove());
			insertRlt.setCommenCover(moveDto.getCommenCover());
			insertRlt.setCommenQuality(moveDto.getCommenQuality());
			insertRlt.setAsynchronousCover(moveDto.getAsynchronousCover());
			insertRlt.setAsynchronousQuality(moveDto.getAsynchronousQuality());
			insertRlt.setRnaUpdateCover(moveDto.getRnaUpdateCover());
			insertRlt.setRnaUpdateSample(moveDto.getRnaUpdateSample());
			this.create(insertRlt);
		}else{
			find.setGateType(moveDto.getGateType());
			find.setDescription(moveDto.getDescription());
			find.setTimeHighSaMove(moveDto.getTimeHighSaMove());
	        find.setTimeLowSaMove(moveDto.getTimeLowSaMove());
	        find.setCommenCover(moveDto.getCommenCover());
	        find.setCommenQuality(moveDto.getCommenQuality());
	        find.setAsynchronousCover(moveDto.getAsynchronousCover());
	        find.setAsynchronousQuality(moveDto.getAsynchronousQuality());
	        find.setRnaUpdateCover(moveDto.getRnaUpdateCover());
	        find.setRnaUpdateSample(moveDto.getRnaUpdateSample());
	        this.update(find);
		}

    }
	public ExceptionEventASAMoveDto queryData() {
		Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)1));
		Iads5gEmbbGateConfig find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		ExceptionEventASAMoveDto eea = new ExceptionEventASAMoveDto();
		if(find!=null && find.getGateType()==1){
			BeanUtils.copyProperties(find,eea);
		}
		return eea;
	}
}
