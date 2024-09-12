/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2023 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Reflection;

import java.util.HashSet;

public class Buff extends Actor {
	
	public Char target;
	{
		actPriority = BUFF_PRIO; // 低优先级，通常在回合结束时处理
	}

	// 确定buff展示时的类型
	public enum buffType {POSITIVE, NEGATIVE, NEUTRAL}
	public buffType type = buffType.NEUTRAL; // 默认的buff类型为中性

	// 是否展示buff的名称
	public boolean announced = false; // 默认为不展示

	// 是否在复活效果中持续存在
	public boolean revivePersists = false; // 默认为不持续

	protected HashSet<Class> resistances = new HashSet<>(); // 抵抗的技能类型集合

	public HashSet<Class> resistances() {
		return new HashSet<>(resistances); // 返回抵抗技能类型的集合副本
	}

	protected HashSet<Class> immunities = new HashSet<>(); // 免疫的技能类型集合

	public HashSet<Class> immunities() {
		return new HashSet<>(immunities); // 返回免疫技能类型的集合副本
	}

	public boolean attachTo(Char target) {

		if (target.isImmune(getClass())) {
			return false; // 如果目标对当前buff免疫，则不能附加buff
		}

		this.target = target; // 设置目标

		if (target.add(this)) {
			if (target.sprite != null) fx(true); // 如果目标有精灵，调用视觉效果
			return true; // 成功附加buff
		} else {
			this.target = null; // 附加失败，清空目标
			return false;
		}
	}

	public void detach() {
		if (target.remove(this) && target.sprite != null) fx(false); // 移除buff，并调用视觉效果
	}

	@Override
	public boolean act() {
		diactivate(); // 执行buff的去激活操作
		return true;
	}

	public int icon() {
		return BuffIndicator.NONE; // 返回图标的标识，默认无图标
	}

	// 某些buff可能需要给图标的基础纹理上色
	public void tintIcon(Image icon) {
		// 默认不做任何操作
	}

	// 渐隐的百分比 (0-1)，通常在buff即将过期时使用
	public float iconFadePercent() {
		return 0; // 默认不渐隐
	}

	// 大图标在桌面UI中显示的文本
	public String iconTextDisplay() {
		return ""; // 默认无文本
	}

	// 通常附加在buff目标角色精灵上的视觉效果
	public void fx(boolean on) {
		// 默认不做任何操作
	}
	public static boolean shouldHandleMessage = false; // 控制是否处理消息的静态标志

	public String heroMessage() {
		String msg = Messages.get(this, "heromsg"); // 获取角色消息
		if (msg.isEmpty()) {
			return null; // 消息为空时返回 null
		} else if (shouldHandleMessage) {
			return null; // 如果应处理消息标志为 true，返回 null
		} else {
			return msg; // 否则返回消息
		}
	}

	public String name() {
		return Messages.get(this, "name"); // 获取角色名称
	}

	public String desc() {
		return Messages.get(this, "desc"); // 获取角色描述
	}

	protected String dispTurns(float input) {
		return Messages.decimalFormat("#.##", input); // 格式化并返回浮点数的字符串表示
	}

	public float visualcooldown() {
		return cooldown() + 1f; // 返回视觉上冷却时间，通常为实际冷却时间加 1
	}

	public static<T extends Buff> T append(Char target, Class<T> buffClass) {
		T buff = Reflection.newInstance(buffClass); // 创建指定类型的 buff 实例
		buff.attachTo(target); // 将 buff 附加到目标
		return buff; // 返回创建的 buff 实例
	}

	public static<T extends FlavourBuff> T append(Char target, Class<T> buffClass, float duration) {
		T buff = append(target, buffClass); // 创建并附加 buff 实例
		buff.spend(duration * target.resist(buffClass)); // 设置 buff 的持续时间
		return buff; // 返回创建的 buff 实例
	}

	public static<T extends Buff> T affect(Char target, Class<T> buffClass) {
		T buff = target.buff(buffClass); // 获取目标上已有的 buff 实例
		if (buff != null) {
			return buff; // 如果 buff 已存在，则返回它
		} else {
			return append(target, buffClass); // 否则创建并附加新的 buff 实例
		}
	}

	public static<T extends FlavourBuff> T affect(Char target, Class<T> buffClass, float duration) {
		T buff = affect(target, buffClass); // 获取或创建 buff 实例
		buff.spend(duration * target.resist(buffClass)); // 设置 buff 的持续时间
		return buff; // 返回 buff 实例
	}

	public static<T extends FlavourBuff> T prolong(Char target, Class<T> buffClass, float duration) {
		T buff = affect(target, buffClass); // 获取或创建 buff 实例
		buff.postpone(duration * target.resist(buffClass)); // 延长 buff 的持续时间
		return buff; // 返回 buff 实例
	}

	public static<T extends CounterBuff> T count(Char target, Class<T> buffclass, float count) {
		T buff = affect(target, buffclass); // 获取或创建 buff 实例
		buff.countUp(count); // 增加 buff 的计数值
		return buff; // 返回 buff 实例
	}

	public static void detach(Char target, Class<? extends Buff> cl) {
		for (Buff b : target.buffs(cl)) { // 遍历目标上的所有指定类型的 buff
			b.detach(); // 从目标上移除每个 buff
		}
	}
}
