/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.webconsole.core.page;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.karaf.webconsole.core.BasePage;
import org.apache.karaf.webconsole.core.WebConsoleTest;
import org.apache.karaf.webconsole.core.brand.DefaultBrandProvider;
import org.apache.karaf.webconsole.core.navigation.ConsoleTabProvider;
import org.apache.karaf.webconsole.core.preferences.util.JdkPreferencesService;
import org.apache.karaf.webconsole.core.test.AlwaysAuthenticatedWebSession;
import org.apache.karaf.webconsole.core.test.LinkAnswer;
import org.apache.karaf.webconsole.core.test.LinksAnswer;
import org.apache.wicket.Page;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * Test secured page and navigation rendering.
 */
@Ignore("This test will be replaced by module tab panel test")
@RunWith(BlockJUnit4ClassRunner.class)
public class SinglePageTest extends WebConsoleTest {

    /**
     * Test only one tab without children.
     */
    @Test
    public void testOneTab() {
        Map<String, Object> values = new HashMap<String, Object>();

        values.put("brandProvider", new DefaultBrandProvider());
        values.put("preferencesService", new JdkPreferencesService());

        List<ConsoleTabProvider> tabs = new ArrayList<ConsoleTabProvider>();

        SerializableConsoleTabProvider mock = createStrictMock(SerializableConsoleTabProvider.class);
        expect(mock.getModuleLink(anyString(), anyString())).andAnswer(new LinkAnswer("test", SinglePageExt.class));
        expect(mock.getItems(anyString(), anyString())).andReturn(emptyLinkList());

        tabs.add(mock);
        values.put("tabs", tabs);
        injector.setValues(values);

        replay(mock);

        WicketTester tester = new WicketTester(application);
        tester.startPage(SinglePageExt.class);
        tester.debugComponentTrees();

        tester.clickLink("topPanel:tabs:0:moduleLink");

        assertTabs(tester, tabs);
        assertTabLink(tester, 0, "test", SinglePageExt.class);


        verify(mock);
    }

    /**
     * Test only one tab, but with two children.
     */
    @Test
    public void testOneTabWithChildren() {
        Map<String, Object> values = new HashMap<String, Object>();

        values.put("brandProvider", new DefaultBrandProvider());
        values.put("preferencesService", new JdkPreferencesService());

        List<ConsoleTabProvider> tabs = new ArrayList<ConsoleTabProvider>();

        SerializableConsoleTabProvider mock = createStrictMock(SerializableConsoleTabProvider.class);
        LinksAnswer answer = new LinksAnswer();
        answer.addLink("A1", SecuredPage.class);
        answer.addLink("B1", SidebarPage.class);
        expect(mock.getItems(anyString(), anyString())).andAnswer(answer);
        expect(mock.getModuleLink(anyString(), anyString())).andAnswer(new LinkAnswer("test", BasePage.class));

        tabs.add(mock);
        values.put("tabs", tabs);
        injector.setValues(values);

        replay(mock);

        WicketTester tester = new WicketTester(application);
        tester.startPage(SecuredPage.class);

        assertTabs(tester, tabs);
        assertTabLink(tester, 0, "test", BasePage.class, answer.getPageLinks());

        tester.assertLabel("topPanel:tabs:0:moduleLinks:0:link:label", "A1");
        tester.assertLabel("topPanel:tabs:0:moduleLinks:1:link:label", "B1");

        verify(mock);
    }

    /**
     * Test four tabs without children.
     */
    @Test
    public void testTwoTabs() {
        Map<String, Object> values = new HashMap<String, Object>();

        values.put("brandProvider", new DefaultBrandProvider());
        values.put("preferencesService", new JdkPreferencesService());

        List<ConsoleTabProvider> tabs = new ArrayList<ConsoleTabProvider>();

        SerializableConsoleTabProvider mock = createStrictMock(SerializableConsoleTabProvider.class);
        expect(mock.getItems(anyString(), anyString())).andReturn(emptyLinkList());
        expect(mock.getModuleLink(anyString(), anyString())).andAnswer(new LinkAnswer("test1", BasePage.class));
        replay(mock);
        tabs.add(mock);

        mock = createStrictMock(SerializableConsoleTabProvider.class);
        expect(mock.getItems(anyString(), anyString())).andReturn(emptyLinkList());
        expect(mock.getModuleLink(anyString(), anyString())).andAnswer(new LinkAnswer("test2", SecuredPage.class));
        replay(mock);
        tabs.add(mock);

        mock = createStrictMock(SerializableConsoleTabProvider.class);
        expect(mock.getItems(anyString(), anyString())).andReturn(emptyLinkList());
        expect(mock.getModuleLink(anyString(), anyString())).andAnswer(new LinkAnswer("test3", SinglePage.class));
        replay(mock);
        tabs.add(mock);


        mock = createStrictMock(SerializableConsoleTabProvider.class);
        expect(mock.getItems(anyString(), anyString())).andReturn(emptyLinkList());
        expect(mock.getModuleLink(anyString(), anyString())).andAnswer(new LinkAnswer("test4", SidebarPage.class));
        replay(mock);
        tabs.add(mock);

        values.put("tabs", tabs);
        injector.setValues(values);

        WicketTester tester = new WicketTester(application);
        tester.startPage(SecuredPage.class);

        assertTabs(tester, tabs);
        assertTabLink(tester, 0, "test1", BasePage.class);
        assertTabLink(tester, 1, "test2", SecuredPage.class);
        assertTabLink(tester, 2, "test3", SinglePage.class);
        assertTabLink(tester, 3, "test4", SidebarPage.class);

        verify(mock);
    }

    // additional asserts

    private void assertTabs(WicketTester tester, List<ConsoleTabProvider> tabs) {
        tester.assertListView("topPanel:tabs", tabs);
    }

    private void assertTabLink(WicketTester tester, int position, String label, Class<? extends WebPage> page) {
        assertTabLink(tester, position, label, page, Collections.<Link<Page>>emptyList());
    }

    private void assertTabLink(WicketTester tester, int position, String label, Class<? extends WebPage> page, List<Link<Page>> children) {
        tester.assertLabel("topPanel:tabs:" + position + ":moduleLink:moduleLabel", label);
        tester.assertBookmarkablePageLink("topPanel:tabs:" + position + ":moduleLink", page, "");
        if (!children.isEmpty()) {
            tester.assertListView("tabs:moduleLinks", children);
        }
    }

    @Override
    protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
        return AlwaysAuthenticatedWebSession.class;
    }

    // Marker interface for tests, normally serialization is controlled by paxwicket
    // as ConsoleTabProviders are OSGi services.
    interface SerializableConsoleTabProvider extends Serializable, ConsoleTabProvider {}

    public static class SinglePageExt extends SinglePage {}
}