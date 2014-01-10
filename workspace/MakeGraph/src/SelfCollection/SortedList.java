package SelfCollection;

import java.util.LinkedList;

public class SortedList
{

	LinkedList<Long> elementdataArrayList;

	public SortedList()
	{
		elementdataArrayList = new LinkedList<Long>();
	}

	public long get(int index)
	{
		return elementdataArrayList.get(index);
	}

	public Boolean add(Long item)
	{
		if (elementdataArrayList.size() == 0)
		{
			elementdataArrayList.add(item);
		} else
		{
			if(elementdataArrayList.get(0)==item)
				return false;
			if(elementdataArrayList.get(elementdataArrayList.size()-1)==item)
				return false;
			if (elementdataArrayList.getFirst() > item)
			{
				elementdataArrayList.addFirst(item);
			} else if (elementdataArrayList.getLast() < item)
			{
				elementdataArrayList.addLast(item);
			} else
			{
				int index = bisearch(item);
				if (index > 0 && index < elementdataArrayList.size() + 1)
				{
					elementdataArrayList.add(bisearch(item), item);
				} else
				{
					if (index < 0 && index > -(elementdataArrayList.size() - 1))
					{
						return false;
					}
				}

			}
		}
		return true;

	}
	
	public int size()
	{
		return elementdataArrayList.size();
	}

	private int bisearch(long item)
	{
		int index = 0;
		int low = 0;
		int high = elementdataArrayList.size() - 1;
		return Search(low, high, item);
		// return index;
	}

	private int Search(int low, int high, long item)
	{
		int middle = (low + high) / 2;
		if (elementdataArrayList.get(low) > item)
		{
			return low;
		} else if (elementdataArrayList.get(high) < item)
		{
			return high + 1;
		}
		if (item == elementdataArrayList.get(middle))
		{
			return -middle;
		}
		while (high - low > 1)
		{
			if (elementdataArrayList.get(middle) < item)
			{
				return Search(middle + 1, high, item);
			} else
			{
				return Search(low, middle - 1, item);
			}
		}
		if (item == elementdataArrayList.get(low))
		{
			return -low;
		}
		if (item == elementdataArrayList.get(high))
		{
			return -high;
		}
		if (item < elementdataArrayList.get(low))
		{
			return low;
		} else if (item < elementdataArrayList.get(high))
		{
			return high;
		}

		else
		{
			return high + 1;
		}
	}
}