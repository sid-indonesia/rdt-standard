package io.ona.rdt.robolectric.fragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.fragment.CovidPatientVisitFragment;
import io.ona.rdt.viewholder.CovidPatientVisitViewHolder;

/**
 * Created by Vincent Karuri on 21/08/2020
 */
public class CovidPatientVisitFragmentTest extends FragmentRobolectricTest {

    private CovidPatientVisitFragment covidPatientVisitFragment;
    private FragmentScenario<CovidPatientVisitFragment> fragmentScenario;

    @Before
    public void setUp() {
        fragmentScenario = FragmentScenario.launchInContainer(CovidPatientVisitFragment.class, null,
                R.style.AppTheme, null);
        fragmentScenario.onFragment(
                fragment -> {
                    covidPatientVisitFragment = fragment;
                }
        );
    }

    @Test
    public void testOnCreateViewShouldCorrectlyPopulatePatientVisits() {
        final int visitOne = 1;
        final int visitTwo = 2;
        final int visitThree = 3;
        final int visitFour = 4;

        RecyclerView visitList = covidPatientVisitFragment.getView()
                .findViewById(R.id.covid_patient_visit_list);
        assertListValuesAreCorrect(visitList, visitOne);
        assertListValuesAreCorrect(visitList, visitTwo);
        assertListValuesAreCorrect(visitList, visitThree);
        assertListValuesAreCorrect(visitList, visitFour);
    }

    private void assertListValuesAreCorrect(RecyclerView visitList, int position) {
        CovidPatientVisitViewHolder viewHolder = (CovidPatientVisitViewHolder) visitList
                .findViewHolderForAdapterPosition(position - 1);
        Assert.assertEquals(String.format("visit%d", position), viewHolder.getTvVisitName().getText());
        Assert.assertEquals(String.format("date%d", position), viewHolder.getTvDateOfVisit().getText());
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }
}