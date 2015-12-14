package org.droidmate.uia_manual_test_cases;

import com.android.uiautomator.core.UiObjectNotFoundException;

public class AtMarkushiExpensemanagerTestCases extends TestCases
{

  private final String appPackageName    = "at.markushi.expensemanager";
  private final String appLaunchIconText = "Expense Manager";

  public void test_useCase_addAndEditExpense() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "addAndEditExpense", 13, false);
  }

  /** Prerequisite: there is one expense.
   * If you run "addAndEditExpense" use case this prerequisite will be fulfilled.
   * Do not reset the app before use case as it will wipe out the expense. Instead, force-stop it, to ensure monitor will restart.
   */
  public void test_useCase_deleteExpenseFromHistory() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "deleteExpenseFromHistory", 4, false);
  }

  public void test_useCase_viewAndSetBudget() throws UiObjectNotFoundException
  {
    executeTestCase(appPackageName, appLaunchIconText, "viewAndSetBudget", 5, false);
  }


}
