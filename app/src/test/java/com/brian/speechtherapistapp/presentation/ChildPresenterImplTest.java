package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.models.ChildList;
import com.brian.speechtherapistapp.repository.IChildRepository;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChildPresenterImplTest {

    private IChildRepository repoMock;
    private IChildPresenter presenter;
    private Child childOne, childTwo;
    private ChildList children;

    @Before
    public void setUp() {

        repoMock = mock(IChildRepository.class);

        childOne = Child.builder("Billy", "Jean", "billy@email.com")
                .build();

        childTwo = Child.builder("Tom", "Jones", "tom@email.com")
                .build();

        when(repoMock.getChildWithEmailIdentifier("billy@email.com")).thenReturn(childOne);
        when(repoMock.getChildWithEmailIdentifier("tom@email.com")).thenReturn(childTwo);

        children = new ChildList();
        children.add(childOne);
        children.add(childTwo);
        when(repoMock.getChildListFromDB()).thenReturn(children);

        presenter = new ChildPresenterImpl(repoMock);
    }


    @Test
    public void testGetByEmail() {
        assertThat(presenter.getChildWithEmail("billy@email.com").getEmail(),
                equalTo(childOne.getEmail()));

        assertThat(presenter.getChildWithEmail("tom@email.com").getEmail(),
                equalTo(childTwo.getEmail()));
    }

    @Test
    public void testGetAll() {
        assertThat(presenter.getChildren().getChildList(), hasSize(2));
        assertThat(presenter.getChildren(), equalTo(children));
    }

}