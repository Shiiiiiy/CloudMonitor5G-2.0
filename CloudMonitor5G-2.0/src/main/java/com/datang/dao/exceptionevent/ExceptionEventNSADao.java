package com.datang.dao.exceptionevent;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventNSADto;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 15:46
 */
@Repository
public class ExceptionEventNSADao extends GenericHibernateDao<Iads5gEmbbGateConfig,Long> {
    @Autowired
    private Iads5gEmbbGateConfig config;
    public void updatePrams(ExceptionEventNSADto eventDto) {
        /*BeanUtils.copyProperties(eventNSADto,config);
        config.setCreateTime(new Date());
        this.create(config);*/
    	Iads5gEmbbGateConfig find = new Iads5gEmbbGateConfig();
    	Iads5gEmbbGateConfig insertRlt = new Iads5gEmbbGateConfig();
    	Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)3));
		find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		if(find==null || find.getGateType()!=3){
			insertRlt.setGateType(eventDto.getGateType());
			insertRlt.setDescription(eventDto.getDescription());
			insertRlt.setTimeHighNSA(eventDto.getTimeHighNSA());
			insertRlt.setTimeLowNSA(eventDto.getTimeLowNSA());
			insertRlt.setDiffLteDiffNrCover(eventDto.getDiffLteDiffNrCover());
			insertRlt.setDiffLteDiffNrQuality(eventDto.getDiffLteDiffNrQuality());
			insertRlt.setDiffLteSameNrCover(eventDto.getDiffLteSameNrCover());
			insertRlt.setDiffLteSameNrQuality(eventDto.getDiffLteSameNrQuality());
			insertRlt.setSameLteDiffNrCover(eventDto.getSameLteDiffNrCover());
			insertRlt.setSameLteDiffNrQuality(eventDto.getSameLteDiffNrQuality());
			insertRlt.setScellAddCover(eventDto.getScellAddCover());
			insertRlt.setScellAddQuality(eventDto.getScellAddQuality());
			insertRlt.setEndcWirelessCover(eventDto.getEndcWirelessCover());
			insertRlt.setEndcWirelessQuality(eventDto.getEndcWirelessQuality());
			this.create(insertRlt);
		}else{
			find.setGateType(eventDto.getGateType());
			find.setDescription(eventDto.getDescription());
			find.setTimeHighNSA(eventDto.getTimeHighNSA());
	    	find.setTimeLowNSA(eventDto.getTimeLowNSA());
	    	find.setDiffLteDiffNrCover(eventDto.getDiffLteDiffNrCover());
	    	find.setDiffLteDiffNrQuality(eventDto.getDiffLteDiffNrQuality());
	    	find.setDiffLteSameNrCover(eventDto.getDiffLteSameNrCover());
	    	find.setDiffLteSameNrQuality(eventDto.getDiffLteSameNrQuality());
	    	find.setSameLteDiffNrCover(eventDto.getSameLteDiffNrCover());
	    	find.setSameLteDiffNrQuality(eventDto.getSameLteDiffNrQuality());
	    	find.setScellAddCover(eventDto.getScellAddCover());
	    	find.setScellAddQuality(eventDto.getScellAddQuality());
	    	find.setEndcWirelessCover(eventDto.getEndcWirelessCover());
	    	find.setEndcWirelessQuality(eventDto.getEndcWirelessQuality());
	        this.update(find);
		}
    }
    
    public ExceptionEventNSADto queryData() {
		Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)3));
		Iads5gEmbbGateConfig find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		ExceptionEventNSADto eea = new ExceptionEventNSADto();
		if(find!=null && find.getGateType()==3){
			BeanUtils.copyProperties(find,eea);
		}
		return eea;
	}
}
