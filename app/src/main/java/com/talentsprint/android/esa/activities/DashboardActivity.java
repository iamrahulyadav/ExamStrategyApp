package com.talentsprint.android.esa.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.apps.talentsprint.R;
import com.talentsprint.android.esa.TalentSprintApp;
import com.talentsprint.android.esa.fragments.AlertsFragment;
import com.talentsprint.android.esa.fragments.DashboardFragment;
import com.talentsprint.android.esa.fragments.MyExamsFragment;
import com.talentsprint.android.esa.fragments.NotificationsFragment;
import com.talentsprint.android.esa.fragments.ProfileFragment;
import com.talentsprint.android.esa.fragments.StratergyFragment;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.ServiceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;

public class DashboardActivity extends FragmentActivity implements DashboardActivityInterface, View.OnClickListener {

    private ImageView header_curve;
    private ImageView menu;
    private ProgressBar progressBar;
    private View progressBarView;
    private String examDate;
    private boolean isStratergyReady;
    private String status;
    private TalentSprintApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);
        findViews();
        apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment(), AppConstants.DASHBOARD).commit();
    }

    private void findViews() {
        menu = findViewById(R.id.menu);
        header_curve = findViewById(R.id.header_curve);
        progressBar = findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        header_curve.setVisibility(View.GONE);
        menu.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
        progressBarView.setVisibility(View.GONE);
    }

    @Override
    public void setCurveVisibility(boolean isShow) {
        if (isShow) {
            header_curve.setVisibility(View.VISIBLE);
        } else {
            header_curve.setVisibility(View.GONE);
        }
    }

    @Override
    public void showProgress(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            progressBarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void examAdded() {
        popAllFragments();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment(), AppConstants.DASHBOARD).commit();
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    private void popAllFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    @Override
    public TalentSprintApi getApiService() {
        if (apiService == null)
            apiService =
                    ApiClient.getCacheClient().create(TalentSprintApi.class);
        return apiService;
    }

    @Override
    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    @Override
    public void isStratergyReady(boolean isStratergyReady) {
        this.isStratergyReady = isStratergyReady;
    }

    @Override
    public void onBackPressed() {
        Fragment initialFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (initialFragment != null) {
            String tag = initialFragment.getTag();
            if (tag.equalsIgnoreCase(AppConstants.QUIZ_RESULT)) {
                examAdded();
                return;
            } else if (tag.equalsIgnoreCase(AppConstants.MY_EXAMS) || tag.equalsIgnoreCase(AppConstants.ARTICLE)) {
                if (TalentSprintApp.refreshDashBorad) {
                    examAdded();
                    TalentSprintApp.refreshDashBorad = false;
                    return;
                }
            }
        }
        super.onBackPressed();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null) {
            String tag = currentFragment.getTag();
            if (tag != null && (tag.equalsIgnoreCase(AppConstants.DASHBOARD) || tag.equalsIgnoreCase(AppConstants.QUIZ_RESULT))) {
                setCurveVisibility(false);
            } else {
                setCurveVisibility(true);
            }
        }
    }

    @Override
    public void onClick(final View view) {
        if (view == menu) {
            showMenu();
        }
    }

    private void openMyExams() {
        if (new ServiceManager(this).isNetworkAvailable())
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MyExamsFragment(), AppConstants.MY_EXAMS).addToBackStack(null).commit();
        else
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
    }

    private void showMenu() {
        final Dialog menuItem = new Dialog(DashboardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        menuItem.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(150, 0, 0, 0)));
        menuItem.setContentView(R.layout.menu_dialogue);
        menuItem.setCanceledOnTouchOutside(true);
        View content = menuItem.findViewById(R.id.content);
        View footer = menuItem.findViewById(R.id.footer);
        View examStratergy = menuItem.findViewById(R.id.examStratergy);
        TextView nextExamDateView = menuItem.findViewById(R.id.nextExamDate);
        if (examDate != null) {
            nextExamDateView.setText(examDate);
        } else {
            nextExamDateView.setText("Not Set");
        }
        content.setOnClickListener(this);
        footer.setOnClickListener(this);
        final View mainView = menuItem.findViewById(R.id.mainView);
        final TextView studyMaterial = menuItem.findViewById(R.id.studyMaterial);
        final TextView currentAffairs = menuItem.findViewById(R.id.currentAffairs);
        final TextView myProfile = menuItem.findViewById(R.id.myProfile);
        final TextView home = menuItem.findViewById(R.id.home);
        final TextView notifications = menuItem.findViewById(R.id.notifications);
        final TextView examAlerts = menuItem.findViewById(R.id.examAlerts);
        final TextView myExams = menuItem.findViewById(R.id.myExams);
        final View myExamsSelector = menuItem.findViewById(R.id.myExamsSelector);
        final View homeSelector = menuItem.findViewById(R.id.homeSelector);
        final View stratergySelector = menuItem.findViewById(R.id.stratergySelector);
        final View studySelector = menuItem.findViewById(R.id.studySelector);
        final View affairsSelector = menuItem.findViewById(R.id.affairsSelector);
        final View alertsSelector = menuItem.findViewById(R.id.alertsSelector);
        final View notificationSelector = menuItem.findViewById(R.id.notificationSelector);
        final View profileSelector = menuItem.findViewById(R.id.profileSelector);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        homeSelector.setVisibility(View.INVISIBLE);
        stratergySelector.setVisibility(View.INVISIBLE);
        studySelector.setVisibility(View.INVISIBLE);
        affairsSelector.setVisibility(View.INVISIBLE);
        alertsSelector.setVisibility(View.INVISIBLE);
        notificationSelector.setVisibility(View.INVISIBLE);
        profileSelector.setVisibility(View.INVISIBLE);
        myExamsSelector.setVisibility(View.INVISIBLE);
        if (currentFragment != null && currentFragment.getTag() != null) {
            switch (currentFragment.getTag()) {
                case AppConstants.DASHBOARD:
                    homeSelector.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.STRATERGY:
                    stratergySelector.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.PROFILE:
                    profileSelector.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.NOTIFICATION:
                    notificationSelector.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.ALERT:
                    alertsSelector.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.MY_EXAMS:
                    myExamsSelector.setVisibility(View.VISIBLE);
                    break;
            }
        }
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.dismiss();
            }
        });
        menuItem.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.dismiss();
            }
        });
        studyMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.dismiss();
                if (status != null && status.equalsIgnoreCase(AppConstants.STRATERGY_IS_READY)) {
                    Intent navigate = new Intent(DashboardActivity.this, StudyMaterialActivity.class);
                    startActivity(navigate);
                } else if (status != null && status.equalsIgnoreCase(AppConstants.EXAM_NOT_SET))
                    showAddExamPopUp("Please set the exam");
                else if (status != null && status.equalsIgnoreCase(AppConstants.ASESMENT_NOT_TAKEN))
                    showAddExamPopUp("Please write the Assessment test");
                else if (status != null && status.equalsIgnoreCase(AppConstants.PREPARING_STRATERGY))
                    showAddExamPopUp("Please wait for a while");

            }
        });
        nextExamDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyExams();
                menuItem.dismiss();
            }
        });
        myExams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMyExams();
                menuItem.dismiss();
            }
        });
        currentAffairs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate = new Intent(DashboardActivity.this, CurrentAffairsActivity.class);
                startActivity(navigate);
                menuItem.dismiss();
            }
        });
        examStratergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStratergyReady) {
                    openStrategy();
                    menuItem.dismiss();
                } else {
                    Toast.makeText(DashboardActivity.this, "Strategy is not prepared", Toast.LENGTH_SHORT).show();
                }
            }
        });
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, new NotificationsFragment(), AppConstants.NOTIFICATION).addToBackStack(null)
                        .commit();
                menuItem.dismiss();
            }
        });
        examAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragment_container, new AlertsFragment(), AppConstants.ALERT).addToBackStack(null)
                        .commit();
                menuItem.dismiss();
            }
        });
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PreferenceManager.getBoolean(DashboardActivity.this, AppConstants.IS_LOGGEDIN, false)) {
                    navigateToProfile();
                } else {
                    Intent navigate = new Intent(DashboardActivity.this, LoginActivity.class);
                    startActivityForResult(navigate, AppConstants.LOGIN_RESULT_PROFILE);
                }
                menuItem.dismiss();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuItem.dismiss();
                examAdded();
            }
        });
        menuItem.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Animation bottomUp = AnimationUtils.loadAnimation(DashboardActivity.this,
                        R.anim.slide_down);
                mainView.startAnimation(bottomUp);
            }
        });
        menuItem.show();
    }

    private void showAddExamPopUp(String message) {
        final Dialog finishDialogue;
        finishDialogue = new Dialog(DashboardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        finishDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(150, 0, 0, 0)));
        finishDialogue.setContentView(R.layout.dialogue_exam_not_set);
        TextView yes = finishDialogue.findViewById(R.id.yes);
        TextView contentText = finishDialogue.findViewById(R.id.contentText);
        contentText.setText(message);
        View mainView = finishDialogue.findViewById(R.id.mainView);
        finishDialogue.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDialogue.dismiss();
            }
        });
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDialogue.dismiss();
            }
        });

    }

    private void navigateToProfile() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment(), AppConstants.PROFILE).addToBackStack(null)
                .commit();
    }

    private void openStrategy() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, new StratergyFragment(), AppConstants.STRATERGY).addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.LOGIN_RESULT_PROFILE && resultCode == RESULT_OK) {
            navigateToProfile();
        }
    }
}
