/*
 * Copyright (c) 2008-2019 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.company.demo.web;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.actions.picker.ClearAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

@UiController("demo_User.edit")
@UiDescriptor("user-edit.xml")
@EditedEntityContainer("userDc")
@LoadDataBeforeShow
public class UserEdit extends StandardEditor<User> {

    @Inject
    private CollectionContainer<Role> rolesDc;
    @Inject
    private Notifications notifications;
    @Inject
    private Dialogs dialogs;

    @Named("groupField.clear")
    private ClearAction groupFieldClear;

    @Install(to = "rolesTable.add", subject = "selectValidator")
    protected boolean rolesTableAddSelectValidator(LookupScreen.ValidationContext<Role> context) {
        System.out.println("rolesTable.add selectValidator: " + context);
        for (Role role : context.getSelectedItems()) {
            if (rolesDc.containsItem(role)) {
                notifications.create().withCaption(role.getName() + " is already selected").show();
                return false;
            }
        }
        return true;
    }

    @Install(to = "rolesTable.add", subject = "transformation")
    protected Collection<Role> rolesTableAddTransformation(Collection<Role> collection) {
        System.out.println("rolesTable.add transformation: " + collection);
        return collection;
    }

    @Install(to = "userRolesTable.exclude", subject = "afterActionPerformedHandler")
    protected void usersTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent event) {
        System.out.println("userRolesTable.exclude AfterActionPerformed: " + event);
    }

    @Install(to = "userRolesTable.exclude", subject = "actionCancelledHandler")
    protected void usersTableRemoveActionCancelledHandler(RemoveOperation.ActionCancelledEvent event) {
        System.out.println("userRolesTable.exclude ActionCancelled: " + event);
    }

    @Install(to = "groupField.lookup", subject = "selectValidator")
    protected boolean groupFieldLookupSelectValidator(LookupScreen.ValidationContext<Group> context) {
        System.out.println("groupField.lookup selectValidator: " + context);
        return true;
    }

    @Install(to = "groupField.lookup", subject = "transformation")
    protected Collection<Group> groupFieldLookupTransformation(Collection<Group> collection) {
        System.out.println("groupField.lookup transformation: " + collection);
        return collection;
    }

    @Install(to = "groupField.lookup", subject = "screenOptionsSupplier")
    protected ScreenOptions groupFieldLookupScreenOptionsSupplier() {
        System.out.println("groupField.lookup screenOptionsSupplier");
        return new MapScreenOptions(ParamsMap.of("p1", 10));
    }

    @Install(to = "groupField.lookup", subject = "screenConfigurer")
    protected void groupFieldLookupScreenConfigurer(Screen lookupScreen) {
        System.out.println("groupField.lookup screenConfigurer: " + lookupScreen);
    }

    @Install(to = "groupField.lookup", subject = "afterCloseHandler")
    protected void groupFieldLookupAfterCloseHandler(AfterCloseEvent event) {
        System.out.println("groupField.lookup AfterClose: " + event);
    }

    @Install(to = "groupField.open", subject = "screenConfigurer")
    protected void groupFieldOpenScreenConfigurer(Screen lookupScreen) {
        System.out.println("groupField.open screenConfigurer: " + lookupScreen);
    }

    @Install(to = "groupField.open", subject = "afterCloseHandler")
    protected void groupFieldOpenAfterCloseHandler(AfterCloseEvent event) {
        System.out.println("groupField.open AfterClose: " + event);
    }

    @Subscribe("groupField.clear")
    private void onGroupFieldClear(Action.ActionPerformedEvent event) {
        dialogs.createOptionDialog()
                .withMessage("Clear?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES).withHandler(e ->
                                groupFieldClear.execute()),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();
    }
    
    
}