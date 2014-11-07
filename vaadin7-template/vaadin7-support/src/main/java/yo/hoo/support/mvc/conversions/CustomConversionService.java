package yo.hoo.support.mvc.conversions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Component;

@Component("customerConversionService")
public class CustomConversionService extends FormattingConversionServiceFactoryBean {

	@Autowired
	public void setConverter() {
		HashSet<Converter<?, ?>> converters = new HashSet<Converter<?, ?>>();
		converters.add(new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				dateFormat.setLenient(false);
				try {
					return dateFormat.parse(source);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null;
			}
		});
		setConverters(converters);
	}
}
