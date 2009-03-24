#include "server.h"

struct fire
explode_bomb(const int x, const int y, const int id, const int len)
{
struct fire f;
int j, k;

	f.x = x;
	f.y = y;

	for (j = x, k = 0; j > 0 && k < len; --j, ++k) {
		if (check_block(x,j)) 
			break;
		kill_if_here(x,j);
		send_map(grid);
	}
	f.up = k;	
	for (j = x, k = 0; j < 15 && k < len; ++j, ++k) {
		if (check_block(x,j))
			break;
		kill_if_here(x,j);
		send_map(grid);
	}
	f.down = k;
	for (j = x, k = 0; j > 0 && k < len; --j, ++k) {
		if (check_block(j,y))
			break;
		kill_if_here(j,y);
		send_map(grid);
	}
	f.left = k;
	for (j = x, k = 0; j < 15 && k < len; ++j, ++k) {
		if (check_block(j,y))
			break;
		kill_if_here(j,y);
		send_map(grid);
	}
	f.right = k;
	return f;
}

void
kill_if_here(const int x, const int y)
{
int i;
	
	for (i = 0; i < MAX_PLAYERS; ++i) {
		if (player_array[i].x_ == x && player_array[i].y_ == y) {
			player_array[i].alive_ = 0;
			send_player_class(player_array[i]);
		}
	}
}

bool
check_block(const int x, const int y)
{
int i;

	if (grid[x][y] == GRID_UBLOCK)
		return true;
	else if (grid[x][y] == GRID_DBLOCK) {
		destroy_block(x,y);
		return true;
	}
	return false;
}

void
destroy_block(const int x, const int y)
{
int i;
	
	i = rand() * 9; // powerup 1/3 of the time
	if (i == 0)
		grid[x][y] = GRID_POWUP_NUM;
	else if (i == 1)
		grid[x][y] = GRID_POWUP_RAD;
	else if (i == 2)
		grid[x][y] = GRID_POWUP_SPIKE;
	else
		grid[x][y] = GRID_EMTPY;
}
