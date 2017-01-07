package ch.christofbuechi.delaywarner.base.dagger;

import ch.christofbuechi.delaywarner.CheckFragment;
import ch.christofbuechi.delaywarner.MainActivity;

/**
 * Created by Christof on 07.01.2017.
 */
public interface InjectionComponent {
    void inject(CheckFragment checkFragment);

    void inject(MainActivity mainActivity);
}
