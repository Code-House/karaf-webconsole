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
package org.apache.karaf.webconsole.core;

import java.util.Collections;
import java.util.List;

import org.apache.karaf.webconsole.core.internal.SidebarPanel;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.util.ListModel;

public class SidebarPage extends BasePage {

    private Panel sidebar;

    public SidebarPage() {
        ListModel<Class<? extends Page>> listModel = new ListModel<Class<? extends Page>>(getSubPages());

        sidebar = new SidebarPanel("sidebar", getClass(), listModel);
        add(sidebar);
    }

    public Panel getSidebar() {
        return sidebar;
    }

    protected List<Class<? extends Page>> getSubPages() {
        return Collections.emptyList();
    }

}
