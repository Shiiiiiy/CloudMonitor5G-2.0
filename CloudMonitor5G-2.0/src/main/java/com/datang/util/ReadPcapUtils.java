package com.datang.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;

/*
 * sturct pcap_file_header
 {
 DWORD              magic;
 WORD                 version_major;
 WORD                 version_minor;
 DWORD              thiszone;
 DWORD              sigfigs;
 DWORD              snaplen;
 DWORD              linktype;
 }
 说明：
 1、标识位：32位的，这个标识位的值是16进制的 0xa1b2c3d4。
 a 32-bit        magic number ,The magic number has the value hex a1b2c3d4.
 2、主版本号：16位， 默认值为0x2。
 a 16-bit          major version number,The major version number should have the value 2.
 3、副版本号：16位，默认值为0x04。
 a 16-bit          minor version number,The minor version number should have the value 4.
 4、区域时间：32位，实际上该值并未使用，因此可以将该位设置为0。
 a 32-bit          time zone offset field that actually not used, so you can (and probably should) just make it 0;
 5、精确时间戳：32位，实际上该值并未使用，因此可以将该值设置为0。
 a 32-bit          time stamp accuracy field tha not actually used,so you can (and probably should) just make it 0;
 6、数据包最大长度：32位，该值设置所抓获的数据包的最大长度，如果所有数据包都要抓获，将该值设置为65535； 例如：想获取数据包的前64字节，可将该值设置为64。
 a 32-bit          snapshot length&quot; field;The snapshot length field should be the maximum number of bytes perpacket that will be captured. If the entire packet is captured, make it 65535; if you only capture, for example, the first 64 bytes of the packet, make it 64.
 7、链路层类型：32位， 数据包的链路层包头决定了链路层的类型。
 a 32-bit link layer type field.The link-layer type depends on the type of link-layer header that the
 packets in the capture file have:
 以下是数据值与链路层类型的对应表
 0            BSD       loopback devices, except for later OpenBSD
 1            Ethernet, and Linux loopback devices   以太网类型，大多数的数据包为这种类型。
 6            802.5 Token Ring
 7            ARCnet
 8            SLIP
 9            PPP
 10          FDDI
 100        LLC/SNAP-encapsulated ATM
 101        raw IP, with no link
 102        BSD/OS SLIP
 103        BSD/OS PPP
 104        Cisco HDLC
 105        802.11
 108        later OpenBSD loopback devices (with the AF_value in network byte order)
 113               special Linux cooked capture
 114               LocalTalk

 struct pcap_pkthdr
 {
 struct timeval         ts;
 DWORD              caplen;
 DWORD              len;
 }

 struct timeval
 {
 DWORD       GMTtime;
 DWORD       microTime
 } 
 说明：
 1、时间戳，包括：
 秒计时：32位，一个UNIX格式的精确到秒时间值，用来记录数据包抓获的时间，记录方式是记录从格林尼治时间的1970年1月1日 00:00:00 到抓包时经过的秒数；
 毫秒计时：32位， 抓取数据包时的毫秒值。
 a time stamp, consisting of:
 a UNIX-format time-in-seconds when the packet was captured, i.e. the number of seconds since January 1,1970, 00:00:00 GMT (that GMT, *NOT* local time!);   
 the number of microseconds since that second when the packet was captured;
 2、数据包长度：32位 ，标识所抓获的数据包保存在pcap文件中的实际长度，以字节为单位。
 a 32-bit value giving the number of bytes of packet data that were captured;
 3、数据包实际长度： 所抓获的数据包的真实长度，如果文件中保存不是完整的数据包，那么这个值可能要比前面的数据包长度的值大。
 a 32-bit value giving the actual length of the packet, in bytes (which may be greater than the previous number, if you are not saving the entire packet).
 */

public class ReadPcapUtils {

	public static void main(String[] args) throws Exception {
		String fileName = "E:\\项目\\iADS\\aa7110000007007416100002\\aa7110000007024789100005.pcap";
		ReadPcapUtils.readDates(new File(fileName));

	}

	/**
	 * 获取pcap文件的时间戳,精确到毫秒
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static List<Date> readDates(File file) throws Exception {
		List<Date> dates = new ArrayList<>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			if (fis.available() != 0) {
				FileChannel fc = fis.getChannel();
				MappedByteBuffer mappedByteBuffer = fc.map(MapMode.READ_ONLY,
						0, fc.size());
				IoBuffer buffer = IoBuffer.wrap(mappedByteBuffer);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				// 文件头，24字节
				buffer.skip(24);
				while (buffer.hasRemaining()) {
					// 数据包头timeval，8字节
					// buffer.skip(8);
					long GMTtime = buffer.getInt();
					long microTime = buffer.getInt();
					dates.add(new Date(GMTtime * 1000 + microTime / 1000));
					// 数据包头caplen, 4字节
					int caplen = buffer.getInt();
					// 数据包头len, 4字节
					int len = buffer.getInt();
					// pcap文件会出现最后一条记录被截断的问题
					byte[] stream = null;
					try {
						stream = new byte[caplen];
						buffer.get(stream);
					} catch (Exception e) {
						System.out.println(e);
						break;
					}
				}
			}
		} finally {
			if (null != fis) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return dates;
	}
}
