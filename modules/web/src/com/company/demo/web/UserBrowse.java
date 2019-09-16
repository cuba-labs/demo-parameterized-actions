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

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.RemoveOperation;
import com.haulmont.cuba.gui.actions.list.CreateAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.screen.*;
import com.haulmont.cuba.security.entity.User;

import javax.inject.Inject;
import javax.inject.Named;

@UiController("demo_User.browse")
@UiDescriptor("user-browse.xml")
@LookupComponent("usersTable")
@LoadDataBeforeShow
public class UserBrowse extends StandardLookup<User> {

    @Inject
    private Dialogs dialogs;

    @Inject
    private Metadata metadata;

    @Named("usersTable.create1")
    private CreateAction<User> usersTableCreate1;

    @Named("usersTable.create2")
    private CreateAction<User> usersTableCreate2;

    @Install(to = "usersTable.create1", subject = "newEntitySupplier")
    protected User usersTableCreate1NewEntitySupplier() {
        User user = metadata.create(User.class);
        user.setName("a user");
        return user;
    }

    @Install(to = "usersTable.create2", subject = "initializer")
    protected void usersTableCreate2Initializer(User entity) {
        entity.setName("new user");
    }

    @Install(to = "usersTable.create1", subject = "afterCloseHandler")
    protected void usersTableCreate1AfterCloseHandler(AfterCloseEvent event) {
        System.out.println("usersTable.create1 AfterClose: " + event);
    }

    @Install(to = "usersTable.create1", subject = "afterCommitHandler")
    protected void usersTableCreate1AfterCommitHandler(User entity) {
        System.out.println("usersTable.create1 AfterCommit: " + entity);
    }

    @Install(to = "usersTable.edit", subject = "afterCloseHandler")
    protected void usersTableEditAfterCloseHandler(AfterCloseEvent event) {
        System.out.println("usersTable.edit AfterClose: " + event);
    }

    @Install(to = "usersTable.edit", subject = "afterCommitHandler")
    protected void usersTableEditAfterCommitHandler(User entity) {
        System.out.println("usersTable.edit AfterCommit: " + entity);
    }

    @Install(to = "usersTable.view", subject = "afterCloseHandler")
    protected void usersTableViewAfterCloseHandler(AfterCloseEvent event) {
        System.out.println("usersTable.view AfterClose: " + event);
    }

    @Install(to = "usersTable.remove", subject = "afterActionPerformedHandler")
    protected void usersTableRemoveAfterActionPerformedHandler(RemoveOperation.AfterActionPerformedEvent event) {
        System.out.println("usersTable.remove AfterActionPerformed: " + event);
    }

    @Install(to = "usersTable.remove", subject = "actionCancelledHandler")
    protected void usersTableRemoveActionCancelledHandler(RemoveOperation.ActionCancelledEvent event) {
        System.out.println("usersTable.remove ActionCancelled: " + event);
    }

    @Subscribe
    private void onInit(InitEvent event) {
        usersTableCreate1.setOpenMode(OpenMode.DIALOG);

//        usersTableCreate1.setNewEntitySupplier(() -> {
//            User user = metadata.create(User.class);
//            user.setName("a user");
//            return user;
//        });

//        usersTableCreate2.setInitializer(entity -> {
//            entity.setName("new user");
//        });

//        usersTableCreate1.setAfterCloseHandler(afterCloseEvent -> {
//            CloseAction closeAction = afterCloseEvent.getCloseAction();
//            System.out.println("Closed with " + closeAction);
//        });
//        usersTableCreate1.setAfterCommitHandler(entity -> {
//            System.out.println("Created " + entity);
//        });
    }

    @Subscribe("usersTable.create2")
    private void onUsersTableCreate2(Action.ActionPerformedEvent event) {
        dialogs.createOptionDialog().withCaption("Confirm").withMessage("Are you sure you want to create?")
                .withActions(
                        new DialogAction(DialogAction.Type.YES).withHandler(e -> {
                            usersTableCreate2.execute();
                        }),
                        new DialogAction(DialogAction.Type.NO)
                )
                .show();
    }
}