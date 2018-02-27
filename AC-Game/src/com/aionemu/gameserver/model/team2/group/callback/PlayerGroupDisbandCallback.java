/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * Credits goes to all Open Source Core Developer Groups listed below
 * Please do not change here something, ragarding the developer credits, except the "developed by XXXX".
 * Even if you edit a lot of files in this source, you still have no rights to call it as "your Core".
 * Everybody knows that this Emulator Core was developed by Aion Lightning 

 * @-Aion-Lightning
 * @Goong_ADM

 

 */
package com.aionemu.gameserver.model.team2.group.callback;

import com.aionemu.commons.callbacks.Callback;
import com.aionemu.commons.callbacks.CallbackResult;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;

/**
 * @author ATracer
 */
@SuppressWarnings("rawtypes")
public abstract class PlayerGroupDisbandCallback implements Callback {

    @Override
    public CallbackResult beforeCall(Object obj, Object[] args) {
        onBeforeGroupDisband((PlayerGroup) args[0]);
        return CallbackResult.newContinue();
    }

    @Override
    public CallbackResult afterCall(Object obj, Object[] args, Object methodResult) {
        onAfterGroupDisband((PlayerGroup) args[0]);
        return CallbackResult.newContinue();
    }

    @Override
    public Class<? extends Callback> getBaseClass() {
        return PlayerGroupDisbandCallback.class;
    }

    public abstract void onBeforeGroupDisband(PlayerGroup group);

    public abstract void onAfterGroupDisband(PlayerGroup group);
}
