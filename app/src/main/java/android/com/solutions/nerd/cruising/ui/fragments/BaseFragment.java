package android.com.solutions.nerd.cruising.ui.fragments;
import android.com.solutions.nerd.cruising.ui.TutorialStep;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
/**
 * Created by mookie on 3/5/17.
 * for Nerd.Solutions
 */

public class BaseFragment extends DialogFragment {
    public String tutorialStepIdentifier;
    public String tutorialText;
    public Unbinder unbinder;
    private TransactionListener<TutorialStep> tutorialStepTransactionListener = new TransactionListener<TutorialStep>() {
        @Override
        public void onResultReceived(TutorialStep step) {
            if (step != null && !step.getWasCompleted() && (step.getDisplayedOn() == null || (new Date().getTime() - step.getDisplayedOn().getTime()) > 86400000)) {
                DisplayTutorialEvent event = new DisplayTutorialEvent();
                event.step = step;
                event.tutorialText = tutorialText;
                EventBus.getDefault().post(event);
            }
        }

        @Override
        public boolean onReady(BaseTransaction<TutorialStep> baseTransaction) {
            return true;
        }

        @Override
        public boolean hasResult(BaseTransaction<TutorialStep> baseTransaction, TutorialStep step) {
            return true;
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (this.tutorialStepIdentifier != null) {
                new Select().from(TutorialStep.class).where(Condition.column("identifier").eq(tutorialStepIdentifier)).async().querySingle(tutorialStepTransactionListener);
            }

            String displayedClassName = this.getDisplayedClassName();

            if (displayedClassName != null) {
                Map<String, Object> additionalData = new HashMap<>();
                additionalData.put("page", displayedClassName);
                AmplitudeManager.sendEvent("navigate", AmplitudeManager.EVENT_CATEGORY_NAVIGATION, AmplitudeManager.EVENT_HITTYPE_PAGEVIEW, additionalData);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectFragment(((BaseActivity) getActivity()).getHabiticaApplication().getComponent());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Receive Events
        try {
            EventBus.getDefault().register(this);
        } catch (EventBusException ignored) {

        }

        return null;
    }

    public abstract void injectFragment(AppComponent component);

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (unbinder == null) {
            unbinder = ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onDestroyView() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }

        super.onDestroyView();
    }

    public String getDisplayedClassName() {
        return this.getClass().getSimpleName();
    }
}
