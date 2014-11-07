/**
 * 
 */
package yo.hoo.support.event;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author yohoo
 *
 */
@SuppressWarnings("serial")
public class EventRouter implements Serializable {
	
	public interface Event extends Serializable {
	}
	
	public interface Listener extends Serializable {
	}
	
	private LinkedHashSet<EventListener> listenerList = null;

	public void addListener(Class<? extends Event> eventType, Listener target, String methodName) throws java.lang.IllegalArgumentException {
		try {
			Method method = target.getClass().getDeclaredMethod(methodName, eventType);
			addListener(eventType, target, method);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e.getCause().getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e.getCause().getMessage());
		}
	}

	public void addListener(Class<? extends Event> eventType, Listener target, Method method) {
		if (listenerList == null) {
			listenerList = new LinkedHashSet<EventListener>();
		}
		listenerList.add(new EventListener(eventType, target, method));
	}

	public void removeListener(Class<? extends Event> eventType, Listener target, String methodName) {
		try {
			Method method = target.getClass().getDeclaredMethod(methodName, eventType);
			removeListener(eventType, target, method);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e.getCause().getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e.getCause().getMessage());
		}
	}

	public void removeListener(Class<? extends Event> eventType, Listener target, Method method) {
		if (listenerList != null) {
			final Iterator<EventListener> i = listenerList.iterator();
			while (i.hasNext()) {
				final EventListener lm = i.next();
				if (lm.matches(eventType, target, method)) {
					i.remove();
					return;
				}
			}
		}
	}

	public void removeAllListeners() {
		listenerList = null;
	}

	public void fireEvent(Event event) {
		if (listenerList != null) {
			final Object[] listeners = listenerList.toArray();
			for (int i = 0; i < listeners.length; i++) {
				((EventListener) listeners[i]).receiveEvent(event);
			}
		}
	}

	public boolean hasListeners(Class<? extends Event> eventType) {
		if (listenerList != null) {
			for (EventListener lm : listenerList) {
				if (lm.isType(eventType)) {
					return true;
				}
			}
		}
		return false;
	}

	public Collection<? extends Listener> getListeners(Class<? extends Event> eventType) {
		List<Listener> listeners = new ArrayList<Listener>();
		if (listenerList != null) {
			for (EventListener lm : listenerList) {
				if (lm.isOrExtendsType(eventType)) {
					listeners.add(lm.getTarget());
				}
			}
		}
		return listeners;
	}

	public class MethodException extends RuntimeException implements Serializable {

		private MethodException(String message, Throwable cause) {
			super(message, cause);
		}

	}
	
	private class EventListener implements Serializable {

		private final Class<? extends Event> eventType;
		private final Listener target;
		private transient Method method;
		private Object[] arguments;

		public EventListener(Class<? extends Event> eventType, Listener target, Method method) throws java.lang.IllegalArgumentException {
			if (!method.getDeclaringClass().isAssignableFrom(target.getClass())) {
				throw new java.lang.IllegalArgumentException("The method "
						+ method.getName()
						+ " cannot be used for the given target: "
						+ target.getClass().getName());
			}

			this.eventType = eventType;
			this.target = target;
			this.method = method;

			final Class<?>[] params = method.getParameterTypes();

			if (params.length == 1
					&& params[0].isAssignableFrom(eventType)) {
				arguments = new Object[] { null };
			} else {
				throw new IllegalArgumentException("Method requires unknown parameters");
			}
		}

		public void receiveEvent(Event event) {
			if (eventType.isAssignableFrom(event.getClass())) {
				try {
					if (arguments.length == 1) {
						method.invoke(target, new Object[] { event });
					} else {
						throw new IllegalArgumentException("Method requires unknown parameters");
					}
				} catch (final java.lang.IllegalAccessException e) {
					// This should never happen
					throw new java.lang.RuntimeException("Internal error - please report", e);
				} catch (final java.lang.reflect.InvocationTargetException e) {
					// An exception was thrown by the invocation target. Throw it forwards.
					throw new MethodException("Invocation of method "
							+ method.getName()
							+ " in "
							+ target.getClass().getName()
							+ " failed.", e.getTargetException());
				}
			}
		}

		public boolean matches(Class<? extends Event> eventType, Listener target, Method method) {
			return (this.target == target)
					&& (eventType.equals(this.eventType) && method.equals(this.method));
		}

		public boolean isType(Class<? extends Event> eventType) {
			return this.eventType == eventType;
		}

		public boolean isOrExtendsType(Class<? extends Event> eventType) {
			return eventType.isAssignableFrom(this.eventType);
		}

		public Listener getTarget() {
			return target;
		}

	}

}
