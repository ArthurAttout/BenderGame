package be.henallux.masi.bendergame.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import be.henallux.masi.bendergame.model.Mode;

public class CreateGameViewModel extends ViewModel {

    public final MutableLiveData<Mode> availableModes = new MutableLiveData<>();

}
