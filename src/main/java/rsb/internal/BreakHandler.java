package rsb.internal;

import rsb.botLauncher.RuneLite;

import java.util.Random;

public class BreakHandler {

	private final Random random = new Random();

	private long nextBreak;
	private long breakEnd;
	private int ticks = 0;
	private final RuneLite bot;
	private boolean checked = false;
	private boolean result = false;

	public BreakHandler(RuneLite bot) {
		this.bot = bot;
	}

	public boolean isBreaking() {
		return ticks > 50 && nextBreak > 0 && nextBreak < System.currentTimeMillis()
				&& breakEnd > System.currentTimeMillis() && can();
	}

	private boolean can() {
		if (checked) {
			return result;
		} else {
			checked = true;
			result = bot.getScriptHandler().onBreak();
			return result;
		}
	}

	public void tick() {
		++ticks;
		if (checked) {
			checked = false;
			bot.getScriptHandler().onBreakResume();
		}
		if (nextBreak < 0 || nextBreak - System.currentTimeMillis() < -30000) {
			ticks = 0;
			int offset = random(20, 120) * 60000;
			nextBreak = System.currentTimeMillis() + offset;
			if (random(0, 4) != 0) {
				breakEnd = nextBreak + random(2, 40) * 60000 + offset / 6;
			} else {
				breakEnd = nextBreak + random(10, 60) * 1000;
			}
		}
	}

	public long getBreakTime() {
		return breakEnd - System.currentTimeMillis();
	}

	private int random(int min, int max) {
		int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random.nextInt(n));
	}

}
