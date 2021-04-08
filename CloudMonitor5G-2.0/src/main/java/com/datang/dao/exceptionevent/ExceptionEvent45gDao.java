package com.datang.dao.exceptionevent;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.datang.common.dao.GenericHibernateDao;
import com.datang.domain.exceptionevent.Iads5gEmbbGateConfig;
import com.datang.web.action.action5g.exceptionevent.dto.ExceptionEvent45gDto;

/**
 * @author chengzhzh@datangmobile.com
 * @create 2019-09-18 8:56
 */
@Repository
public class ExceptionEvent45gDao extends GenericHibernateDao<Iads5gEmbbGateConfig,Long> {
    public void updateDataOfExceptionEventSAMoveparams(ExceptionEvent45gDto moveDto) {
        /*BeanUtils.copyProperties(moveDto,gateConfig);
        gateConfig.setCreateTime(new Date());
        this.create(gateConfig);*/
    	Iads5gEmbbGateConfig find = new Iads5gEmbbGateConfig();
    	Iads5gEmbbGateConfig insertRlt = new Iads5gEmbbGateConfig();
    	Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)4));
		find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		if(find==null || find.getGateType()!=4){
			insertRlt.setGateType(moveDto.getGateType());
			insertRlt.setDescription(moveDto.getDescription());
			insertRlt.setTimeHigh45g(moveDto.getTimeHigh45g());
			insertRlt.setTimeLow45g(moveDto.getTimeLow45g());
			insertRlt.setSwitchCover4To5G(moveDto.getSwitchCover4To5G());
			insertRlt.setSwitchQuality4To5G(moveDto.getSwitchQuality4To5G());
			insertRlt.setRelocateCover4To5G(moveDto.getRelocateCover4To5G());
			insertRlt.setRelocateQuality4To5G(moveDto.getRelocateQuality4To5G());
			insertRlt.setReselectCover4To5G(moveDto.getReselectCover4To5G());
			insertRlt.setReselectQuality4To5G(moveDto.getReselectQuality4To5G());
			insertRlt.setSwitchCover5To4G(moveDto.getSwitchCover5To4G());
			insertRlt.setSwitchQuality5To4G(moveDto.getSwitchQuality5To4G());
			insertRlt.setRelocateCover5To4G(moveDto.getRelocateCover5To4G());
			insertRlt.setRelocateQuality5To4G(moveDto.getRelocateQuality5To4G());
			insertRlt.setReselectCover5To4G(moveDto.getReselectCover5To4G());
			insertRlt.setReselectQuality5To4G(moveDto.getReselectQuality5To4G());
			this.create(insertRlt);
		}else{
			find.setGateType(moveDto.getGateType());
			find.setDescription(moveDto.getDescription());
			find.setTimeHigh45g(moveDto.getTimeHigh45g());
	        find.setTimeLow45g(moveDto.getTimeLow45g());
	        find.setSwitchCover4To5G(moveDto.getSwitchCover4To5G());
	        find.setSwitchQuality4To5G(moveDto.getSwitchQuality4To5G());
	        find.setRelocateCover4To5G(moveDto.getRelocateCover4To5G());
	        find.setRelocateQuality4To5G(moveDto.getRelocateQuality4To5G());
	        find.setReselectCover4To5G(moveDto.getReselectCover4To5G());
	        find.setReselectQuality4To5G(moveDto.getReselectQuality4To5G());
	        find.setSwitchCover5To4G(moveDto.getSwitchCover5To4G());
	        find.setSwitchQuality5To4G(moveDto.getSwitchQuality5To4G());
	        find.setRelocateCover5To4G(moveDto.getRelocateCover5To4G());
	        find.setRelocateQuality5To4G(moveDto.getRelocateQuality5To4G());
	        find.setReselectCover5To4G(moveDto.getReselectCover5To4G());
	        find.setReselectQuality5To4G(moveDto.getReselectQuality5To4G());
	        this.update(find);
		}
    }
    
	public ExceptionEvent45gDto queryData() {
		Criteria criteria = this.getHibernateSession().createCriteria(Iads5gEmbbGateConfig.class);
		criteria.add(Restrictions.eq("gateType", (short)4));
		Iads5gEmbbGateConfig find = (Iads5gEmbbGateConfig)criteria.uniqueResult();
		ExceptionEvent45gDto eea = new ExceptionEvent45gDto();
		if(find!=null && find.getGateType()==4){
			BeanUtils.copyProperties(find,eea);
		}
		return eea;
	}
}
