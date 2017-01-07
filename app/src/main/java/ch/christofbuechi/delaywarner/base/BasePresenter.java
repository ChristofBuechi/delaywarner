package ch.christofbuechi.delaywarner.base;

/**
 * Created by Christof on 07.01.2017.
 */
public abstract class BasePresenter <T extends BaseUI> implements Presenter<T>{
    private T ui;

    public void setUi(T ui) {
        this.ui = ui;
    }
    public T getUi() {
        return ui;
    }
}
