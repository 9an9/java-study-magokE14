package city;

import java.util.ArrayList;
import java.util.List;

import character.Superman;
import character.civilian.CivilainState;
import character.civilian.Civilian;
import character.villain.Villain;
import character.villain.VillainState;

public class Seoul{
	List<Civilian> civilianList = new ArrayList<>();
	List<Villain> villainList = new ArrayList<>();
	Superman superman;
	
	public Seoul(int civilianCount, int villainCount) {
		for(int i = 0; i < civilianCount; i++) {
			civilianList.add(new Civilian());
		}
		for(int i = 0; i < villainCount; i++) {
			villainList.add(new Villain());
		}
		superman = Superman.getSuperman();
		
		printCityStatus();
	}
	
	public void run() {
		while(true) {
			if(civilianList.size() > 0 && villainList.size() == 0) {
				System.out.println("==============================");
				System.out.println("���۸��� ������ ���߽��ϴ�.");
				System.out.println("==============================");
				break;
			}
			else if(civilianList.size() == 0 && villainList.size() > 0) {
				System.out.println("==============================");
				System.out.println("�Ǵ翡 ���� ���￡ ȥ���� ã�ƿԽ���.");
				System.out.println("==============================");
				break;
			}
			
			// =================================================
			// �Ǵ��� �ù��� ������
			// =================================================
			// �����Ϸ��� �Ǵ� ���ϱ�
			for(Villain villain : villainList) {
				if(villain.getState() == VillainState.ALIVE) {
					for(Civilian civilian : civilianList) {
						if(civilian.getState() == CivilainState.ALIVE) {
							villain.setAttack(civilian);
							break;
						}
					}
					break;
				}
			}
				
			// =================================================
			// ���۸��� �ù��� ������
			// =================================================
			superman.attackVillain();
			
			// =================================================
			// �Ǵ��� �ù��� ������
			// =================================================
			for(Villain villain : villainList) {
				if(villain.getState() == VillainState.ATTACK) {
					villain.attack();
				}
			}
			
			// =================================================
			// �ùε� ��Ȳ ����
			// =================================================
			civilianList.removeIf(civilian -> (civilian.getState() == CivilainState.DEAD));
			
			// =================================================
			// �Ǵ�� ��Ȳ ����
			// =================================================
			villainList.removeIf(villain -> (villain.getState() == VillainState.DEAD));
			

			// =================================================
			// ���� ��Ȳ
			// =================================================
			printCityStatus();
		}
	}
	
	private void printCityStatus() {
		System.out.println("==============================");
		System.out.println("�ù� ��: " + civilianList.size());
		System.out.println("�Ǵ� ��: " + villainList.size());
		System.out.println("==============================");
	}
}
