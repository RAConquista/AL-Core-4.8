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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Avol modified by ATracer
 */
public class CM_EQUIP_ITEM extends AionClientPacket {

    public long slotRead;
    public int itemUniqueId;
    public int action;

    public CM_EQUIP_ITEM(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }

    @Override
    protected void readImpl() {
        PacketLoggerService.getInstance().logPacketCM(this.getPacketName());
        action = readC(); // 0/1 = equip/unequip
        slotRead = readQ();
        itemUniqueId = readD();
    }

    @Override
    protected void runImpl() {
    	
        final Player activePlayer = getConnection().getActivePlayer();
        activePlayer.getController().cancelUseItem();
        
        Equipment equipment = activePlayer.getEquipment();
        Item resultItem = null;
        
        if (!RestrictionsManager.canChangeEquip(activePlayer)) {
            return;
        }

        switch (action) {
            case 0:
                Item targetItem = activePlayer.getInventory().getItemByObjId(itemUniqueId);
                if (targetItem == null) return;
        		if (targetItem.getItemTemplate().getCategory() == ItemCategory.PLUME){
        		}
                resultItem = equipment.equipItem(itemUniqueId, slotRead);
                break;
            case 1:
                resultItem = equipment.unEquipItem(itemUniqueId, slotRead);
                break;
            case 2:
                if (activePlayer.getController().hasTask(TaskId.ITEM_USE) && !activePlayer.getController().getTask(TaskId.ITEM_USE).isDone()) {
                    PacketSendUtility.sendPacket(activePlayer, SM_SYSTEM_MESSAGE.STR_CANT_EQUIP_ITEM_IN_ACTION);
                    return;
                }
                // checking for stance
                if (activePlayer.getController().isUnderStance()){
                    activePlayer.getController().stopStance();
                }
                equipment.switchHands();
                break;
        }

        if (resultItem != null || action == 2) {
            PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(), equipment.getEquippedForApparence()), true);
        }

    }
}
