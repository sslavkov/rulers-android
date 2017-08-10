package com.bgrulers.rulerslist;

import com.bgrulers.BasePresenter;
import com.bgrulers.BaseView;
import com.bgrulers.model.Ruler;

import java.util.List;

/**
 * Created by sslavkov on 3/20/17.
 */

public interface RulerListContract {

	interface View extends BaseView<Presenter> {

		void showRulers(List<Ruler> rulers);
		void showLoading(boolean isLoading);
		void startRulerDetail(Ruler ruler);

	}

	interface Presenter extends BasePresenter {

		void loadRulers();
		void startDetailsActivity(Long rulerId);
		void startDetailsActivity(int position);

	}

}
