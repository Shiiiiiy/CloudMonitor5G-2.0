package com.datang.dao.exceptionevent;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEventBussinessDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 8:56
 */
@Repository
public class ExceptionEventBussinessDao extends GenericHibernateDao<Iads5gEmbbGateConfig,Long> {
    public void updateParams(ExceptionEventBussinessDto moveDto) {
        /*BeanUtils.copyProperties(moveDto,gateConfig);
        gateConfig.setCreateTime(new Date());
        this.create(gateConfig);*/
    	Iads5gEmbbGateConfig find = new Iads5gEmbbGateConfig();
    	Iads5gEmbbGateConfig insertRlt = new Iads5gEmbbGateConfig();
    	Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)5));
		find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		if(find==null || find.getGateType()!=5){
			insertRlt.setGateType(moveDto.getGateType());
			insertRlt.setDescription(moveDto.getDescription());
			insertRlt.setTimeHighBusiness(moveDto.getTimeHighBusiness());
			insertRlt.setTimeLowBusiness(moveDto.getTimeLowBusiness());
			insertRlt.setFtpDLLostConnectCover(moveDto.getFtpDLLostConnectCover());
			insertRlt.setFtpDLLostConnectQuality(moveDto.getFtpDLLostConnectQuality());
			insertRlt.setFtpULLostConnectCover(moveDto.getFtpULLostConnectCover());
			insertRlt.setFtpULLostConnectQuality(moveDto.getFtpULLostConnectQuality());
			insertRlt.setPingFailCover(moveDto.getPingFailCover());
			insertRlt.setPingFailQuality(moveDto.getPingFailQuality());
			this.create(insertRlt);
		}else{
			find.setGateType(moveDto.getGateType());
			find.setDescription(moveDto.getDescription());
			find.setTimeHighBusiness(moveDto.getTimeHighBusiness());
			find.setTimeLowBusiness(moveDto.getTimeLowBusiness());
			find.setFtpDLLostConnectCover(moveDto.getFtpDLLostConnectCover());
			find.setFtpDLLostConnectQuality(moveDto.getFtpDLLostConnectQuality());
			find.setFtpULLostConnectCover(moveDto.getFtpULLostConnectCover());
			find.setFtpULLostConnectQuality(moveDto.getFtpULLostConnectQuality());
			find.setPingFailCover(moveDto.getPingFailCover());
			find.setPingFailQuality(moveDto.getPingFailQuality());
	        this.update(find);
		}
    }
    
	public ExceptionEventBussinessDto queryData() {
		Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)5));
		Iads5gEmbbGateConfig find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		ExceptionEventBussinessDto eea = new ExceptionEventBussinessDto();
		if(find!=null && find.getGateType()==5){
			BeanUtils.copyProperties(find,eea);
		}
		return eea;
	}
}
