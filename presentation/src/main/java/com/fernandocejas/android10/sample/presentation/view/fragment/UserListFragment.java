/**
 * Copyright (C) 2014 android10.org. All rights reserved.
 *
 * @author Fernando Cejas (the android10 coder)
 */
package com.fernandocejas.android10.sample.presentation.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.fernandocejas.android10.sample.presentation.LogUtil;
import com.fernandocejas.android10.sample.presentation.R;
import com.fernandocejas.android10.sample.presentation.internal.di.components.UserComponent;
import com.fernandocejas.android10.sample.presentation.model.UserModel;
import com.fernandocejas.android10.sample.presentation.presenter.UserListPresenter;
import com.fernandocejas.android10.sample.presentation.view.UserListView;
import com.fernandocejas.android10.sample.presentation.view.adapter.UsersAdapter;
import com.fernandocejas.android10.sample.presentation.view.adapter.UsersLayoutManager;
import java.util.Collection;
import javax.inject.Inject;

/**
 * Fragment that shows a list of Users.
 */
public class UserListFragment extends BaseFragment implements UserListView {
//
  /**
   * Interface for listening user list events.
   */
  public interface UserListListener {
    void onUserClicked(final UserModel userModel);
  }

  @Inject UserListPresenter userListPresenter;
  @Inject UsersAdapter usersAdapter;

  @Bind(R.id.rv_users) RecyclerView rv_users;
  @Bind(R.id.rl_progress) RelativeLayout rl_progress;
  @Bind(R.id.rl_retry) RelativeLayout rl_retry;
  @Bind(R.id.bt_retry) Button bt_retry;

  private UserListListener userListListener;

  public UserListFragment() {
    LogUtil.logTest("constructor setRetainInstance(true)");
    setRetainInstance(true);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    LogUtil.logTest("onAttach(Activity activity)");
    if (activity instanceof UserListListener) {
      LogUtil.logTest("this.userListListener = (UserListListener) activity");
      this.userListListener = (UserListListener) activity;
    }
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    LogUtil.logTest("onCreate this.getComponent(UserComponent.class).inject");
    super.onCreate(savedInstanceState);
    this.getComponent(UserComponent.class).inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LogUtil.logTest("onCreateView");
    final View fragmentView = inflater.inflate(R.layout.fragment_user_list, container, false);
    ButterKnife.bind(this, fragmentView);
    setupRecyclerView();
    return fragmentView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    LogUtil.logTest("onViewCreated");
    super.onViewCreated(view, savedInstanceState);
    LogUtil.logTest("this.userListPresenter.setView(this);");
    this.userListPresenter.setView(this);
    if (savedInstanceState == null) {
      LogUtil.logTest("loadUserList()");
      this.loadUserList();
    }
  }

  @Override
  public void onStart() {
    LogUtil.logTest("onStart ");
    super.onStart();
  }

  @Override public void onResume() {
    LogUtil.logTest("onResume ");
    super.onResume();
    this.userListPresenter.resume();
  }

  @Override public void onPause() {
    LogUtil.logTest("onPause ");
    super.onPause();
    this.userListPresenter.pause();
  }

  @Override public void onDestroyView() {
    LogUtil.logTest("onDestroyView ");
    super.onDestroyView();
    rv_users.setAdapter(null);
    ButterKnife.unbind(this);
  }

  @Override public void onDestroy() {
    LogUtil.logTest("onDestroy ");
    super.onDestroy();
    this.userListPresenter.destroy();
  }

  @Override public void onDetach() {
    LogUtil.logTest("onDetach ");
    super.onDetach();
    this.userListListener = null;
  }

  @Override public void showLoading() {
    LogUtil.logTest("LoadDataView showLoading()");
    this.rl_progress.setVisibility(View.VISIBLE);
    this.getActivity().setProgressBarIndeterminateVisibility(true);
  }

  @Override public void hideLoading() {
    LogUtil.logTest("LoadDataView hideLoading()");
    this.rl_progress.setVisibility(View.GONE);
    this.getActivity().setProgressBarIndeterminateVisibility(false);
  }

  @Override public void showRetry() {
    LogUtil.logTest("LoadDataView showRetry()");
    this.rl_retry.setVisibility(View.VISIBLE);
  }

  @Override public void hideRetry() {
    LogUtil.logTest("LoadDataView hideRetry()");
    this.rl_retry.setVisibility(View.GONE);
  }

  @Override public void renderUserList(Collection<UserModel> userModelCollection) {
    LogUtil.logTest("UserListView renderUserList(Collection<UserModel> userModelCollection)");
    if (userModelCollection != null) {
      LogUtil.logTest(" this.usersAdapter.setUsersCollection(userModelCollection)");
      this.usersAdapter.setUsersCollection(userModelCollection);
    }
  }

  @Override public void viewUser(UserModel userModel) {
    LogUtil.logTest("UserListView viewUser(UserModel userModel)");
    LogUtil.logTest(" this.userListListener.onUserClicked(userModel)");
    if (this.userListListener != null) {
      this.userListListener.onUserClicked(userModel);
    }
  }

  @Override public void showError(String message) {
    LogUtil.logTest("LoadDataView showError(String message)");

    this.showToastMessage(message);
  }

  @Override public Context context() {
    LogUtil.logTest("LoadDataView Context context()");
    return this.getActivity().getApplicationContext();
  }

  private void setupRecyclerView() {
    LogUtil.logTest("private void setupRecyclerView()");
    this.usersAdapter.setOnItemClickListener(onItemClickListener);
    this.rv_users.setLayoutManager(new UsersLayoutManager(context()));
    this.rv_users.setAdapter(usersAdapter);
  }

  /**
   * Loads all users.
   */
  private void loadUserList() {

    LogUtil.logTest("this.userListPresenter.initialize()");
    this.userListPresenter.initialize();
  }

  @OnClick(R.id.bt_retry) void onButtonRetryClick() {
    LogUtil.logTest("this.userListPresenter.initialize()");
    UserListFragment.this.loadUserList();
  }

  private UsersAdapter.OnItemClickListener onItemClickListener =
      new UsersAdapter.OnItemClickListener() {
        @Override public void onUserItemClicked(UserModel userModel) {
          LogUtil.logTest("UsersAdapter.OnItemClickListener onItemClickListener");
          if (UserListFragment.this.userListPresenter != null && userModel != null) {
            UserListFragment.this.userListPresenter.onUserClicked(userModel);
          }
        }
      };
}
