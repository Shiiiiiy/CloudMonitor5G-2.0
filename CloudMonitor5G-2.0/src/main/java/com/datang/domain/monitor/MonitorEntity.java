package com.datang.domain.monitor;

import java.io.Serializable;

/**
 * 监控父类
 * 
 * @explain
 * @name MonitorEntity
 * @author shenyanwei
 * @date 2016年7月11日下午1:45:12
 */
// @Entity
// @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// @Table(name = "IADS_REALTIME_MONITOR_ENTITY")
// @TableGenerator(name = "ME_GEN", table = "IADS_REALTIME_ID_GENERATOR",
// pkColumnName = "ID_NAME", valueColumnName = "ENTITY_ID", pkColumnValue =
// "ENTITY_ID", allocationSize = 1)
public class MonitorEntity implements Serializable {
	// /**
	// *
	// */
	// private static final long serialVersionUID = -6190958626146309914L;
	// /** 主健 */
	// protected Long id;
	// /** 监控实体类型 */
	// protected String monitorType;
	// /** 要终端的终端 */
	// protected Terminal terminal;
	// /** 实体类对应的sessionId */
	// protected String sessionId;
	// /** 模块号（MosValue，alarm，event共有） **/
	// protected String moduleNo;
	// /** 通道类型 **/
	// protected ChannelType channelType;
	// /** 监控数据时间 **/
	// protected Date produceDate;
	//
	// protected GPS gps;
	//
	// /**
	// * @return the sessionId
	// */
	// @Column(name = "SESSION_ID")
	// public String getSessionId() {
	// return sessionId;
	// }
	//
	// /**
	// * @param sessionId
	// * the sessionId to set
	// */
	// public void setSessionId(String sessionId) {
	// this.sessionId = sessionId;
	// }
	//
	// /**
	// * @return the id
	// */
	// @Id
	// @GeneratedValue(strategy = GenerationType.TABLE, generator = "ME_GEN")
	// @Column(name = "ID")
	// public Long getId() {
	// return id;
	// }
	//
	// /**
	// * @param id
	// * the id to set
	// */
	// public void setId(Long id) {
	// this.id = id;
	// }
	//
	// /**
	// * @return the monitorType
	// */
	// @Column(name = "MONITOR_TYPE")
	// public String getMonitorType() {
	// return monitorType;
	// }
	//
	// /**
	// * @param monitorType
	// * the monitorType to set
	// */
	// public void setMonitorType(String monitorType) {
	// this.monitorType = monitorType;
	// }
	//
	// /**
	// * @return the terminal
	// */
	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "TERMINAL_ID")
	// public Terminal getTerminal() {
	// return terminal;
	// }
	//
	// /**
	// * @param terminal
	// * the terminal to set
	// */
	// public void setTerminal(Terminal terminal) {
	// this.terminal = terminal;
	// }
	//
	// /**
	// * @return the gps
	// */
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "GPS_ID")
	// public GPS getGps() {
	// return gps;
	// }
	//
	// /**
	// * @param gps
	// * the gps to set
	// */
	// public void setGps(GPS gps) {
	// this.gps = gps;
	// }
	//
	// /**
	// * @return the moduleNo
	// */
	// @Column(name = "MODULE_NO")
	// public String getModuleNo() {
	// return moduleNo;
	// }
	//
	// /**
	// * @param moduleNo
	// * the moduleNo to set
	// */
	// public void setModuleNo(String moduleNo) {
	// this.moduleNo = moduleNo;
	// }
	//
	// /**
	// * @return the channelType
	// */
	// @Column(name = "CHANNEL_TYPE")
	// public ChannelType getChannelType() {
	// return channelType;
	// }
	//
	// /**
	// * @param channelType
	// * the channelType to set
	// */
	// public void setChannelType(ChannelType channelType) {
	// this.channelType = channelType;
	// }
	//
	// /**
	// * @return the produceDate
	// */
	// @Column(name = "PRODUCE_DATE")
	// public Date getProduceDate() {
	// return produceDate;
	// }
	//
	// /**
	// * @param produceDate
	// * the produceDate to set
	// */
	// public void setProduceDate(Date produceDate) {
	// this.produceDate = produceDate;
	// }

}
