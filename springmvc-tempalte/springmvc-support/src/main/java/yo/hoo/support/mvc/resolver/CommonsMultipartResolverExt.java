package yo.hoo.support.mvc.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import yo.hoo.support.utils.NumUtil;

public class CommonsMultipartResolverExt extends CommonsMultipartResolver {

	private HttpServletRequest request;

	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		FileUpload fileUpload = super.newFileUpload(fileItemFactory);
		if (request != null) {
			fileUpload.setProgressListener(new FileUploadListener(request.getSession()));
		}
		return fileUpload;
	}

	@Override
	public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request) throws MultipartException {
		this.request = request;
		return super.resolveMultipart(request);
	}

	@Override
	protected FileUpload prepareFileUpload(String encoding) {
		FileUpload actualFileUpload = newFileUpload(getFileItemFactory());
		actualFileUpload.setHeaderEncoding(encoding);
		actualFileUpload.setSizeMax(-1);
		return actualFileUpload;
	}

	public class FileUploadListener implements ProgressListener {

		private HttpSession session;

		public FileUploadListener() {
		}

		public FileUploadListener(HttpSession session) {
			this.session = session;
			Progress status = new Progress();
			session.setAttribute("upload_ps", status);
		}

		public void update(long bytesRead, long contentLength, int items) {
			Progress status = (Progress) session.getAttribute("upload_ps");
			status.setBytesRead(bytesRead);
			status.setContentLength(contentLength);
			status.setItems(items);
		}
	}

	public class Progress {

		/** 已读字节 **/
		private long bytesRead = 0L;
		/** 已读MB **/
		private String mbRead = "0";
		/** 总长度 **/
		private long contentLength = 0L;
		/****/
		private int items;
		/** 已读百分比 **/
		private String percent;
		/** 读取速度 **/
		private String speed;
		/** 开始读取的时间 **/
		private long startReatTime = System.currentTimeMillis();

		public long getBytesRead() {
			return bytesRead;
		}

		public void setBytesRead(long bytesRead) {
			this.bytesRead = bytesRead;
		}

		public long getContentLength() {
			return contentLength;
		}

		public void setContentLength(long contentLength) {
			this.contentLength = contentLength;
		}

		public int getItems() {
			return items;
		}

		public void setItems(int items) {
			this.items = items;
		}

		public String getPercent() {
			percent = NumUtil.getPercent(bytesRead, contentLength);
			return percent;
		}

		public void setPercent(String percent) {
			this.percent = percent;
		}

		public String getSpeed() {
			speed = NumUtil.divideNumber(
					NumUtil.divideNumber(bytesRead * 1000, System.currentTimeMillis() - startReatTime),
					1000);
			return speed;
		}

		public void setSpeed(String speed) {
			this.speed = speed;
		}

		public long getStartReatTime() {
			return startReatTime;
		}

		public void setStartReatTime(long startReatTime) {
			this.startReatTime = startReatTime;
		}

		public String getMbRead() {
			mbRead = NumUtil.divideNumber(bytesRead, 1000000);
			return mbRead;
		}

		public void setMbRead(String mbRead) {
			this.mbRead = mbRead;
		}
	}
}