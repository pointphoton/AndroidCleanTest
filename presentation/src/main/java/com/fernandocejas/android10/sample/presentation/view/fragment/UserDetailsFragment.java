/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fernandocejas.android10.sample.presentation.LogUtil;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent;
import com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.fernandocejas.android10.sample.presentation.presenter.UserDetailsPresenter;
import com.fernandocejas.android10.sample.presentation.view.UserDetailsView;
import com.fernandocejas.android10.sample.presentation.view.component.AutoLoadImageView;
import com.fernandocejas.arrow.checks.Preconditions;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Fragment that shows details of a certain user.
 */
public class UserDetailsFragment extends BaseFragment implements UserDetailsView {
  private static final String PARAM_USER_ID = "param_user_id";

  @Inject UserDetailsPresenter userDetailsPresenter;

  @Bind(R.id.iv_cover) AutoLoadImageView iv_cover;
  @Bind(R.id.tv_fullname) TextView tv_fullname;
  @Bind(R.id.tv_email) TextView tv_email;
  @Bind(R.id.tv_followers) TextView tv_followers;
  @Bind(R.id.tv_description) TextView tv_description;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.rl_retry) RelativeLayout rl_retry;
  @Bind(R.id.bt_retry) Button bt_retry;

  public static UserDetailsFragment forUser(int userId) {
    final UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
    final Bundle arguments = new Bundle();
    arguments.putInt(PARAM_USER_ID, userId);
    userDetailsFragment.setArguments(arguments);
    return userDetailsFragment;
  }

  public UserDetailsFragment() {
    LogUtil.logTest("constructor setRetainInstance(true)");
    setRetainInstance(true);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.getComponent(UserComponent.class).inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LogUtil.logTest("onCreateView");
    final View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
    ButterKnife.bind(this, fragmentView);
    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    LogUtil.logTest("this.userDetailsPresenter.setView(this)");
    this.userDetailsPresenter.setView(this);
    if (savedInstanceState == null) {
      this.loadUserDetails();
    }
  }

  @Override public void onResume() {
    super.onResume();
    LogUtil.logTest(" this.userDetailsPresenter.resume()");
    this.userDetailsPresenter.resume();
  }

  @Override public void onPause() {
    LogUtil.logTest("this.userDetailsPresenter.pause()");
    super.onPause();
    this.userDetailsPresenter.pause();
  }

  @Override public void onDestroyView() {
    LogUtil.logTest("onDestroyView() Unbind butterknife");
    super.onDestroyView();
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    super.onDestroy();
    LogUtil.logTest("this.userDetailsPresenter.destroy()");
    this.userDetailsPresenter.destroy();
  }


  @Override public void renderUser(UserModel user) {
    LogUtil.logTest("UserDetailsView renderUser(UserModel user)");
    if (user != null) {
      this.iv_cover.setImageUrl(user.getCoverUrl());
      this.tv_fullname.setText(user.getFullName());
      this.tv_email.setText(user.getEmail());
      this.tv_followers.setText(String.valueOf(user.getFollowers()));
      this.tv_description.setText(user.getDescription());
    }
  }

  @Override public void showLoading() {
    LogUtil.logTest("LoadDataView UserDetailsView showLoading()");
    this.rl_progress.setVisibility(View.VISIBLE);
    this.getActivity().setProgressBarIndeterminateVisibility(true);
  }

  @Override public void hideLoading() {
    LogUtil.logTest("LoadDataView UserDetailsView hideLoading()");
    this.rl_progress.setVisibility(View.GONE);
    this.getActivity().setProgressBarIndeterminateVisibility(false);
  }

  @Override public void showRetry() {
    LogUtil.logTest("LoadDataView UserDetailsView showRetry()");
    this.rl_retry.setVisibility(View.VISIBLE);
  }

  @Override public void hideRetry() {
    LogUtil.logTest("LoadDataView UserDetailsView hideRetry()");
    this.rl_retry.setVisibility(View.GONE);
  }

  @Override public void showError(String message) {
    LogUtil.logTest("LoadDataView UserDetailsView showError()");
    this.showToastMessage(message);
  }

  @Override public Context context() {
    LogUtil.logTest("LoadDataView UserDetailsView context()");
    return getActivity().getApplicationContext();
  }

  /**
   * Load user details.
   */
  private void loadUserDetails() {
    LogUtil.logTest("loadUserDetails()");
    if (this.userDetailsPresenter != null) {
      LogUtil.logTest("userDetailsPresenter.initialize(currentUserId())");
      this.userDetailsPresenter.initialize(currentUserId());
    }
  }

  /**
   * Get current user id from fragments arguments.
   */
  private int currentUserId() {
    final Bundle arguments = getArguments();
    Preconditions.checkNotNull(arguments, "Fragment arguments cannot be null");
    return arguments.getInt(PARAM_USER_ID);
  }

  @OnClick(R.id.bt_retry)
  void onButtonRetryClick() {
    LogUtil.logTest("onButtonRetryClick()");
    LogUtil.logTest("loadUserDetails()");
    UserDetailsFragment.this.loadUserDetails();
  }
}
