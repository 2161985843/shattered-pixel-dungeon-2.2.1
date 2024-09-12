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

package com.shatteredpixel.shatteredpixeldungeon;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;

public class QuickSlot {

	/**
	 * 槽位包含玩家背包中的物品对象。唯一的例外是，当物品数量为0时，这可能是指一种可堆叠物品已经被“用完”，此时它们被称为占位符。
	 */


	//请注意，由于 UI 限制，当前的最大大小编码为 6，但它可能要大得多，没有问题。
	public static int SIZE = 6;
	private Item[] slots = new Item[SIZE];


	//直接数组交互方法，一切都应该从这些方法构建。
	public void setSlot(int slot, Item item){
		clearItem(item); //we don't want to allow the same item in multiple slots.
		slots[slot] = item;
	}

	public void clearSlot(int slot){
		slots[slot] = null;
	}

	public void reset(){
		slots = new Item[SIZE];
	}

	public Item getItem(int slot){
		return slots[slot];
	}

	//utility methods, for easier use of the internal array.
	public int getSlot(Item item) {
		for (int i = 0; i < SIZE; i++) {
			if (getItem(i) == item) {
				return i;
			}
		}
		return -1;
	}

	public Boolean isPlaceholder(int slot){
		return getItem(slot) != null && getItem(slot).quantity() == 0;
	}

	public Boolean isNonePlaceholder(int slot){
		return getItem(slot) != null && getItem(slot).quantity() > 0;
	}

	public void clearItem(Item item){
		if (contains(item)) {
			clearSlot(getSlot(item));
		}
	}

	public boolean contains(Item item){
		return getSlot(item) != -1;
	}

	public void replacePlaceholder(Item item) {
		for (int i = 0; i < SIZE; i++) {
			if (isPlaceholder(i) && item.isSimilar(getItem(i))) {
				setSlot(i, item);
			}
		}
	}

	public void convertToPlaceholder(Item item){

		if (contains(item)) {
			Item placeholder = item.virtual();
			if (placeholder == null) return;

			for (int i = 0; i < SIZE; i++) {
				if (getItem(i) == item) setSlot(i, placeholder);
			}
		}
	}

	public Item randomNonePlaceholder(){

		ArrayList<Item> result = new ArrayList<>();
		for (int i = 0; i < SIZE; i ++) {
			if (getItem(i) != null && !isPlaceholder(i)) {
				result.add(getItem(i));
			}
		}
		return Random.element(result);
	}

	private final String PLACEHOLDERS = "placeholders";
	private final String PLACEMENTS = "placements";

	/**
	 * 放置数组用于捆绑时保留顺序，但精确索引不是，所以如果我们
	 * 捆绑占位符（保留它们的顺序）和一个数组，告诉我们占位符的位置，
	 *我们可以完美地重建它们。
	 */

	public void storePlaceholders(Bundle bundle){
		ArrayList<Item> placeholders = new ArrayList<>(SIZE);
		boolean[] placements = new boolean[SIZE];

		for (int i = 0; i < SIZE; i++) {
			if (isPlaceholder(i)) {
				placeholders.add(getItem(i));
				placements[i] = true;
			}
		}
		bundle.put( PLACEHOLDERS, placeholders );
		bundle.put( PLACEMENTS, placements );
	}

	public void restorePlaceholders(Bundle bundle){
		Collection<Bundlable> placeholders = bundle.getCollection(PLACEHOLDERS);
		boolean[] placements = bundle.getBooleanArray( PLACEMENTS );

		int i = 0;
		for (Bundlable item : placeholders){
			while (!placements[i]){
				i++;
			}
			setSlot( i, (Item)item );
			i++;
		}

	}

}
