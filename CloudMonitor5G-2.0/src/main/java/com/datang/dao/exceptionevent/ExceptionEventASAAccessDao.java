package com.datang.dao.exceptionevent;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventASAAccessDto;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:29
 */
@Repository
public class ExceptionEventASAAccessDao extends GenericHibernateDao<Iads5gEmbbGateConfig,Long> {
    @Autowired
    private Iads5gEmbbGateConfig gateConfig;

    public void updateParams(ExceptionEventASAAccessDto eventDto) {
        /*BeanUtils.copyProperties(eventDto,gateConfig);
        gateConfig.setCreateTime(new Date());
        this.create(gateConfig);*/
    	Iads5gEmbbGateConfig find = new Iads5gEmbbGateConfig();
    	Iads5gEmbbGateConfig insertRlt = new Iads5gEmbbGateConfig();
    	Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)2));
		find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		if(find==null || find.getGateType()!=2){
			insertRlt.setGateType(eventDto.getGateType());
			insertRlt.setDescription(eventDto.getDescription());
			insertRlt.setTimeHighSaAccess(eventDto.getTimeHighSaAccess());
			insertRlt.setTimeLowSaAccess(eventDto.getTimeLowSaAccess());
			insertRlt.setRrcCover(eventDto.getRrcCover());
	    	insertRlt.setRrcQuality(eventDto.getRrcQuality());
	    	insertRlt.setServeBulidCover(eventDto.getServeBulidCover());
	    	insertRlt.setServeBulidQuality(eventDto.getServeBulidQuality());
	    	insertRlt.setRegisterCover(eventDto.getRegisterCover());
	    	insertRlt.setRegisterQuality(eventDto.getRegisterQuality());
	    	insertRlt.setPduSessionConfirmCover(eventDto.getPduSessionConfirmCover());
	    	insertRlt.setPduSessionConfirmQuality(eventDto.getPduSessionConfirmQuality());
	    	this.create(insertRlt);
		}else{
			find.setGateType(eventDto.getGateType());
			find.setDescription(eventDto.getDescription());
			find.setTimeHighSaAccess(eventDto.getTimeHighSaAccess());
	    	find.setTimeLowSaAccess(eventDto.getTimeLowSaAccess());
	    	find.setRrcCover(eventDto.getRrcCover());
	    	find.setRrcQuality(eventDto.getRrcQuality());
	    	find.setServeBulidCover(eventDto.getServeBulidCover());
	    	find.setServeBulidQuality(eventDto.getServeBulidQuality());
	    	find.setRegisterCover(eventDto.getRegisterCover());
	    	find.setRegisterQuality(eventDto.getRegisterQuality());
	    	find.setPduSessionConfirmCover(eventDto.getPduSessionConfirmCover());
	    	find.setPduSessionConfirmQuality(eventDto.getPduSessionConfirmQuality());
	    	this.update(find);
		}
    }
    
    public ExceptionEventASAAccessDto queryData() {
		Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)2));
		Iads5gEmbbGateConfig find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		ExceptionEventASAAccessDto eea = new ExceptionEventASAAccessDto();
		if(find!=null && find.getGateType()==2){
			BeanUtils.copyProperties(find,eea);
		}
		return eea;
	}
}
