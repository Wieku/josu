package pl.tinlink.josu.animation.accessors;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import pl.tinlink.josu.animation.api.AnimationAccessor;

public class CellAccessor implements AnimationAccessor<Cell<?>>{

	@Override
	public int getValues(Cell<?> element, int tweenType, float[] values) {
		values[0] = element.getPrefWidth();
		return 1;
	}

	@Override
	public void setValues(Cell<?> element, int tweenType, float[] newValues) {
		element.size(newValues[0]);
		
	}

}
