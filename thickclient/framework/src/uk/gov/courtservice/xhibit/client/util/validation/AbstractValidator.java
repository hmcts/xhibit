package uk.gov.courtservice.xhibit.client.util.validation;

/**
 * Base class for validators used to validate Swing controls.
 * 
 * @author uphillj
 *
 * @param <T>
 */
public abstract class AbstractValidator<T> implements Validator<T> {

	private Class<T> targetType;
	
	public AbstractValidator(Class<T> targetType) {
		this.targetType = targetType;
	}

	@Override
	public Class<T> getTargetType() {
		return targetType;
	}
}
