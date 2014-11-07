package yo.hoo.core.component;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.vaadin.jouni.animator.Animator;
import org.vaadin.jouni.dom.client.Css;

import yo.hoo.support.widget.ThemeHoo;

import com.vaadin.data.Buffered.SourceException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.BindException;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Page;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;

@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
public class BeanWindow extends Window implements ClickListener {

	private FieldGroup fieldGroup;
	private ClickListener listener;
	private FormLayout formLayout = new FormLayout();
	private Button okButton = new Button("确定", this);
	private Button cancelButton = new Button("取消", this);
	private HorizontalLayout buttonLayout = new HorizontalLayout(okButton, cancelButton);

	private BeanWindow(final Object bean) {
		ThemeHoo.registerTheme(this);
		fieldGroup = new MyFieldGroup(bean.getClass());
		fieldGroup.setFieldFactory(new MyFieldGroupFieldFactory());
		fieldGroup.setItemDataSource(new BeanItem(bean));

		formLayout.setMargin(true);
		formLayout.setSizeUndefined();
		buttonLayout.setSpacing(true);

		VerticalLayout content = new VerticalLayout(formLayout, buttonLayout);
		content.setMargin(true);
		content.setSizeUndefined();
		content.setComponentAlignment(formLayout, Alignment.TOP_CENTER);
		content.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
		Panel panel = new Panel(content);
		panel.addStyleName(ThemeHoo.PANEL_BORDERLESS);
		Animator.animate(panel,
				new Css().setProperty("max-height", Page.getCurrent().getBrowserWindowHeight() * 0.9 + "px")
						.setProperty("overflow", "overlay"));
		setContent(panel);

		center();
		setModal(true);
		setClosable(false);
		setResizable(false);
	}

	public BeanWindow(Object bean, String... propertyIds) {
		this(bean);
		for (int i = 0; i < propertyIds.length; i++) {
			Field<?> field = fieldGroup.buildAndBind(propertyIds[i]);
			formLayout.addComponent(field);
			field.setWidth("12em");
			field.addStyleName(ThemeHoo.HOO_BIG);
		}
	}

	public BeanWindow(Object bean, String[] propertyIds, String[] captions) {
		this(bean);
		for (int i = 0; i < propertyIds.length; i++) {
			Field<?> field = fieldGroup.buildAndBind(captions[i], propertyIds[i]);
			formLayout.addComponent(field);
			field.setWidth("12em");
			field.addStyleName(ThemeHoo.HOO_BIG);
		}
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == okButton) {
			try {
				fieldGroup.commit();
				if (listener != null) {
					listener.buttonClick(event);
				}
				close();
			} catch (SourceException e) {
				fieldGroup.discard();
			} catch (InvalidValueException e) {
				fieldGroup.discard();
			} catch (CommitException e) {
				fieldGroup.discard();
			} catch (Exception e) {
				fieldGroup.discard();
				MessageBox.showHTML(Icon.ERROR, null, "<h2>" + e.getMessage() + "</h2>", ButtonId.ABORT);
			}
		} else if (event.getButton() == cancelButton) {
			fieldGroup.discard();
			close();
		}
	}

	public FieldGroup getFieldGroup() {
		return fieldGroup;
	}

	public FormLayout getFormLayout() {
		return formLayout;
	}

	public void setOkButtonClickListener(ClickListener listener) {
		this.listener = listener;
	}

	public class MyFieldGroup extends BeanFieldGroup {

		private Class beanType;

		public MyFieldGroup(Class beanType) {
			super(beanType);
			this.beanType = beanType;
		}

		@Override
		public Field buildAndBind(String caption, Object propertyId, Class fieldType)
				throws BindException {
			Field field;
			java.lang.reflect.Field f;
			try {
				f = getField(beanType, propertyId.toString());
			} catch (SecurityException e) {
				throw new BindException("Cannot determine type of propertyId '"
						+ propertyId + "'.", e);
			} catch (NoSuchFieldException e) {
				throw new BindException("Cannot determine type of propertyId '"
						+ propertyId + "'. The propertyId was not found in "
						+ beanType.getName(), e);
			}
			if (Enum.class.isAssignableFrom(f.getType())) {
				ComboBox comboBox = new ComboBox(caption);
				for (Object e : EnumSet.allOf((Class<Enum>) f.getType())) {
					comboBox.addItem(e);
				}
				field = comboBox;
			} else {
				Class<?> type = getPropertyType(propertyId);
				field = build(caption, type, fieldType);
			}
			bind(field, propertyId);
			return field;
		}

		private java.lang.reflect.Field getField(Class<?> cls,
				String propertyId) throws SecurityException, NoSuchFieldException {
			if (propertyId.contains(".")) {
				String[] parts = propertyId.split("\\.", 2);
				java.lang.reflect.Field field1 = getField(cls, parts[0]);
				return getField(field1.getType(), parts[1]);
			} else {
				try {
					java.lang.reflect.Field field1 = cls
							.getDeclaredField(propertyId);
					return field1;
				} catch (NoSuchFieldException e) {
					Class<?> superClass = cls.getSuperclass();
					if (superClass != null && superClass != Object.class) {
						return getField(superClass, propertyId);
					} else {
						throw e;
					}
				}
			}
		}

	}

	public class MyFieldGroupFieldFactory extends DefaultFieldGroupFieldFactory implements ValueChangeListener {

		@Override
		public void valueChange(ValueChangeEvent event) {
			Object value = event.getProperty().getValue();
			if (value == null) {
				event.getProperty().setValue("");
			}
		}

		@Override
		protected RichTextArea createRichTextArea() {
			RichTextArea rta = new RichTextArea();
			rta.setImmediate(true);
			rta.addValueChangeListener(this);
			return rta;
		}

		@Override
		protected <T extends AbstractTextField> T createAbstractTextField(
				Class<T> fieldType) {
			if (fieldType == AbstractTextField.class) {
				fieldType = (Class<T>) TextField.class;
			}
			try {
				T field = fieldType.newInstance();
				field.setImmediate(true);
				field.addValueChangeListener(this);
				return field;
			} catch (Exception e) {
				throw new BindException("Could not create a field of type "
						+ fieldType, e);
			}
		}
	}

}