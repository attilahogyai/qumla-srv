package eu.dorsum.webtrader.services.menu.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import eu.dorsum.webtrader.domain.menu.MenuItem;
import eu.dorsum.webtrader.domain.menu.MenuItemRq;
import eu.dorsum.webtrader.services.menu.MenuService;

public class MenuServiceMyBatisImpl implements MenuService{

	@Autowired
	@Qualifier("menuMapper")
	private MenuMapper menuMapper;
	
	@Override
	public List<MenuItem> getMenuItems(MenuItemRq request) {
		return menuMapper.getMenuItems(request);
	}
	
	@Override
	public void createMenu(MenuItem menu) throws Exception {
		menuMapper.createMenu(menu);
	}
	
	@Override
	public void modifyMenu(MenuItem menu) throws Exception {
		menuMapper.modifyMenu(menu);
	}
	
	@Override
	public void deleteMenu(MenuItem menu) throws Exception {
		menuMapper.deleteMenu(menu);
	}

}
