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
package org.apache.karaf.webconsole.blueprint.internal.bundle;

import org.apache.karaf.webconsole.blueprint.internal.IBlueprintBundleStateTracker;
import org.apache.karaf.webconsole.blueprint.internal.details.DetailsPage;
import org.apache.karaf.webconsole.osgi.bundle.IActionProvider;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.osgi.framework.Bundle;

public class BlueprintActionProvider implements IActionProvider {

    private IBlueprintBundleStateTracker tracker;

    public BlueprintActionProvider(IBlueprintBundleStateTracker tracker) {
        this.tracker = tracker;
    }

    public Component create(String componentId, Bundle model) {
        PageParameters params = new PageParameters();
        params.put("bundleId", model.getBundleId());
        BookmarkablePageLink link = new BookmarkablePageLink(componentId, DetailsPage.class, params);
        link.setModel(Model.of("Blueprint"));
        return link;
    }

}
