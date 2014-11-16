package pl.tinlink.josu.api;

public interface State {
	
	public void resize(int width, int height);
	public void render(float delta);
	public void onEnter();
	public void onEscape();
	
}
