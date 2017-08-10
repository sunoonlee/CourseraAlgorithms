# Coursera Algorithms 笔记

- 课程: [Algorithms, Part I | Coursera](https://www.coursera.org/learn/algorithms-part1)
    - Part2 尚未开放. booksite: http://algs4.cs.princeton.edu/
- 书: Algorithms.
  - 这本书的讲解通俗易懂, 非常详细. 比如, 红黑树是用 2-3 tree 来引入, 并且约定使用较为简单的 left-leaning 形式, 大大降低了理解的难度.
- 算法学习方法
  - 刘未鹏 - [知其所以然](http://mindhacks.cn/2008/07/07/the-importance-of-knowing-why/): 阅读这些书之余应该寻找算法的原始出处，应该寻根究底，多做一些功课，知道算法到底是怎么诞生的 
    - 例子: [{Algo}说到红黑树 - Google Groups](https://groups.google.com/forum/#!topic/pongba/NROiEGX6umg)
- 学习小结
  - Part1 前半段的学习, 我是以课程为主教材为辅, 后半段反过来, 发现效果更好一些. 因为书讲得很详细, 并且文字形式更方便咀嚼. 而视频听起来有点平淡 (没有 LFD 那样有感染力).
  - 大部分都在阅读, 尽管会敲一下示例代码, 课程的每周作业也做了4/5, 但书上的大量练习都略过了. 感觉如果只是阅读的话, 容易把一些模糊的地方忽略掉, 误以为都懂了. 如果能适当多做点练习应该会比较好.

## w1 Course Introduction

- course overview
  - focus on both algorithms and data structures
  - part 1: data types, sorting, searching
  - part 2: graphs, strings, advanced
- why study algorithms
  - to solve problems that could not otherwise be addressed.
  - for intellectual stimulation: “great algorithms are the poetry of computation.”
  - to become a proficient programmer
    - Linus: “Bad programmers worry about the code. Good programmers worry about data structures and their relationships.”
    - Niklaus Wirth: “Algorithms + Data Structures = Programs”
  - They may unlock the secrets of life and of the universe.
    - Computational models are replacing math models in scientific inquiry.
    - “Algorithms: a common language for nature, human, and computer. ” — Avi Wigderson
## w1 Union-Find

### Steps to developing a usable algorithm

- Model the problem.
- Find an algorithm to solve it.
- Fast enough? Fits in memory?
- If not, figure out why not.
- Find a way to address the problem.
- Iterate until satisfied.

### Dynamic connectivity problems

- 可应用 union-find 的一类典型问题. 另一类是 week1 作业的 percolation 问题. union-find 是对这一系列问题的抽象.
- 问题特点: 一系列 objects; union 操作可连接两个 objects; find 用于查询两个 objects 是否连通.
- modelling the connections:
  - 连接关系的性质: 自反 reflexive, 对称, 可传递
  - 用 connected components 来 model 连接关系
    - find: 看是否在同一 component 里
    - union: 将两个 object 的 component 合并
- 可用 API: `import edu.princeton.cs.algs4.UF`. 方法 `uf.connected(), uf.union()`

### Union-Find 算法实现

- Quick Find: eager approach
  - 用数组 `id[]`记录 component id
  - 这种方法 find 很快, 只需比较 id 是否相等; 但 union 很慢, 单次 union 需要访问数组 N 次. 
  - N 个元素 N 次 Union 就需要 N^2 次访问. N = 10^9 时, 需要30年!
- Quick-union: lazy approach
  - 数组 `id[]` 用来记录 parent. 形成树状结构.
  - find(p, q): 比较 p, q 是否有相同的 root
  - union(p, q): 把 p’s root id 改为 q’s root id (把 p 的树接到 q 的根节点)
  - find 和 union 都很慢, 时间都浪费在 finding roots 上了.
  - quick-find 是 flat tree, quick-union 是 tall tree
- 咋改进呢
  - quick-find 树太平, quick-union 树太高, 如果能平衡一下会比较好?
  - 改进1: weighted quick-union
    - 增加一个数组 `sz[]` 记录 size. union 时确保较小的树接到较大的树上 → weight by size.
      - 也可用 weight by height
    - 可以证明节点的深度不超过 $log_2N$
  - 改进2: path compression
    - 在寻找节点 p 根节点的过程中, 顺便把经过节点的父节点都设为根节点
    - 改进后 时间复杂度接近线性. 30年减少至 6秒.
  - 都是着眼于把 tall tree 削矮一点: weighting 的想法比较自然. path compression 比较新奇, 是利用 finding root 的时间”顺便”来减小树的高度.

### 另一个 Union-find 应用: percolation (assg1

- [assignment specification](http://coursera.cs.princeton.edu/algs4/assignments/percolation.html) / [my solution](https://github.com/sunoonlee/CourseraAlgorithms/tree/master/src/com/algorithms/assg1)
- 三种状态: blocked, open, full.
- 可以看做一个二维的 connectivity 问题. 解决时还是要转成一维的 UF 对象.
  - ?? 有没有可能直接在二维的层面上解决?
- 一个重要的 trick 是增加 virtual-top 和 virtual-bottom 节点. 否则, 判断 isFull 时需要遍历 top row, 判断 percolates() 时需要遍历 bottom row.
## w1 Analysis of Algorithms
- some algorithmic success 
  -  比如 Discrete Fourier transform 问题上, FFT algorithm 实现了 $N^2 \rightarrow N\log N$. 使大数据量的处理成为可能.
- Knuth 1970s: “Use scientific method to understand performance”
- scientific method: observe → hypothesize → predict → verify → validate

### observations

- running time 受 input 影响相对较小, 可重点关注 problem size 的影响
- **log-log 曲线**往往是直线: $\lg T = c + b \lg N \Rightarrow T = aN^b$  ←  `power law`
  - 快速估计 b: $\lg (T(2N)/T(N)) = b$

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1497501219489_image.png)


### mathematical models

- **total running time = sum of** `**cost x frequency**` **for all operations**
  - Knuth 提出, 理论上可以得到精确的数学模型. 但这一过程和结果都会比较复杂, 因此可以做些简化.
- cost of basic operations: 

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1497579918560_image.png)


![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1497579952466_image.png)

- simplify cost model
  - 程序运行中的诸多 operations, 可选一个 cost 和 frequency 最大的作为代表.
- tilde notation: 忽略低阶项, 因为我们更关心 N 很大的情形.
- 例子: 2-sum 和 3-sum
  - 只看 array accesses 运算. tilde notation 分别为 ~N^2 和 ~1/2 N^3.
- estimating a discrete sum
  - 抛开离散数学, 可以把 sum 换成积分

### order-of-growth classifications

- 典型的 order of growth: $1, \log N, N, N \log N, N^2, N^3, 2^N$

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1497585814026_image.png)

- practical implications of order-of-growth
  - 对二次和三次方的算法, 摩尔定律的效果极其有限.

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1497586326353_image.png)


### theory of algorithms

- coping with dependence on inputs
  - best/worst/average case
- performance guarantees
  - 一种比较极端的做法: 保证 **worst-case performance**
    - 适用于: 时间超限会造成灾难性后果的场景
  - 更温和的做法: introduce randomness, 取一个 **probabilistic guarantee**
    - 比如 quicksort 在 worst case 下是 quadratic, 但概率极小, 可以忽略
  - 当算法涉及 a sequence of operations 时, 可以用 **amortized analysis**
    - 保证平均 cost, 允许少数 expensive operations
    - 比如 resizing array stack: 刚好赶上 resize 时单次操作会是 linear time, 但平均下来仍然是 constant time.
- **notations**
  - **Big Theta**: order of growth, 可用于算法分类
  - **Big O**: order of growth 上限. 取决于当前已知的最优算法.
    - 对 Big O 经常存在误解. 实际上, n, n^2, n^3 都是 O(n^3)
  - **Big Omega**: order of growth 下限. 需要证明 no algorithm can do better.
  - **Tilde**: 函数的高阶项 (包含常数系数), 一种 approximation
- **本课程关注 approximate models 而非 upper bound, 用 Tilde notation.** 

### memory

- 32-bit machine, 4 byte pointers → 64 bit machine. 8 bytes pointers.
- Java 常见类型的内存使用:
  - boolean, byte ~ 1. char ~ 2
  - int, float ~ 4. long, double ~ 8
  - Array: 24 bytes + memory for each array entry.
  - Objects: overhead 16 bytes, reference 8 bytes, padding 8 bytes.
## w2 Stacks and queues

### linked list

- 三种操作很方便:
  - insert at the beginning 
    - `Node oldfirst = first; first = new Node(); first.item = item; first.next = oldfirst;`
  - remove from the beginning
    - `first = first.next;`
  - insert at the end
    - `Node oldlast = last; Node last = new Node(); last.item = item; last.next = null; oldlast.next = last;`
- 而 remove from the end 比较麻烦, 因为无法由单向链表的 last 节点直接获取前一个节点.

遍历链表元素:

    for (Node x = first; x != null; x = x.next) {
      // process x.item.
    }

modular programming

- seperate interface and implementation

### stacks

- 链表方式
  - 单次操作的时间有保证.
  - memory: ~40N
    - object overhead = 16
    - inner class extra overhead = 8
    - references to item and Node = 8*2
- resizing array 方式
  - 用 resizing 解决数组固定长度问题.
  - 此方式总时间更少. 占用的内存较小. amortized case 下更优, 但 worst case 较慢 (刚好赶上 resizing 时)
  - memory: [~8N, ~32N]

### queues

- 链表方式: 比 stack 稍麻烦些, 要在首尾各有一个 reference
- 数组方式: 也比 stack 麻烦一些. 因为两头都在移动.

### generics #Java

- Java 特性. 让算法适应不同的数据类型
    public class Stack<Item> {
      ...
      private class Node {
        Item item;
        Node next;
      }
      ...
    }
- 另一种不那么好的替代方法: 定义时用 Object 类型, 使用时用 cast. 缺点:
  - 需要在 client 里写 cast 代码
  - cast 出的错是我们不大希望的 run-time error. 而 generic 方式是更优的 compile-time error.
- Autoboxing
  - generic 里用到的数据类型必须是 reference type. 如果是 primitive types, 在声明时需要指定为对应的 wrapper type. 如 `Stack<Integer> stack = new Stack<Integer>()`. 其余无需操心, Java 会自动地将在 primitive type 和 wrapper types 之间转换.

### iterators #Java

- 什么是 Iterable 和 Iterator
    public interface Iterable<Item> {
      Iterator<Item> iterator();
    }
    
    public interface Iterator<Item> {
      boolean hasNext();
      Item next();
      void remove();  // 不推荐用
    }
- 为了 Python 式的简洁, 我们需要自己实现 Iterator 接口.
  - 为啥算法课不用 python 来教呢? 这样就不用刻意处理数据类型/iterator/数组变长等问题了.

### applications

- 新手阶段建议少用 Java 现成的数据结构库. 因为里面包含的操作可能比较多, 性能难以评估.
- 例子: 函数调用. 包含了 stack 式的 push 和 pop.
- 例子: arithmetic expression evaluation. 用两个 stack.

### 另: Python 中的 stack 和 queue

- 参 https://docs.python.org/3/tutorial/datastructures.html#more-on-lists
- Python 中的 list 在末尾 append 和 pop 效率比较高. 但在开头 insert 或 pop 比较慢, 因为这样剩下元素的位置都要平移. 因此:
- stack 直接用 list 即可, 在末尾 append/pop
- queue 可以用 `collections.deque` , 用 append 和 popleft 方法
## w2 Elementary sorts

介绍三种基本的 sort 方法. 其中 selection sort 和 insertion sort 都是 $\Theta(N^2)$. shellsort 可以突破 $\Theta(N^2)$. 这三种方法都不占用额外内存. 

### Sorting introduction

- 利用 callbacks 机制, 让 sort() 适应各种类型
  - **callback = refence to executable code**
  - Java 里可以用 interface 实现
    - 把 array of objects 传给 sort()
    - sort() calls back object’s compareTo() method
  - Python 里可以直接把函数作为参数
- comparable API
  - 常见内置数据类型本身就实现了 comparable API. 自定义的类型需要自行实现.
- 一般要求顺序关系为 total order: 满足自反性, 反对称性, 传递性.

### ​​selection sort

- 每一步从右侧选出最小的元素, 与当前元素互换
- ~N^2/2 compares and N exchanges

### insertion sort

- 每一步把当前元素插入到左侧有序序列的合适位置
- 平均: ~N^2/4 compares and ~N^2/4 exchanges
- insertion sort 在两种情况下很高效: 
  - small array. mergesort/quicksort 的改进版就对 small subarrays 使用 insertion sort
  - partially sorted

### shellsort

- 数轮间隔 h 的 insertion sort, h 逐渐减至1
  - 开始 h 较大时, 每个 subarray 数量小, 排序很快
  - h 变小时, array 已经是 partially sorted, 排序也很快
- h-sorted array 在做 g-sort 之后, 仍然是 h-sorted
- increment sequence: 一种简单的选择: h = 3h+1 (Knuth)
- worst-case number of compares: 如果选上面这种 间隔序列, 是 $\Theta(N^{3/2})$. 也可以选一些复杂的间隔序列, 可以更小, 如 $\Theta(N^{6/5})$ .

### ^sorting application: Shuffling

- 一种笨办法: 为每个元素生成一个随机实数, 以此来排序
- Knuth shuffle
  - 对每个元素 i, 从 [0, i] 中随机选一个整数 r, 互换 a[i] 和 a[r]
  - linear time
  - 有点像 insertion sort.
  - 随机数的选择范围也可以是 [i, n-1], 但不能是 [0, n-1] !
- 真正实现一个 random shuffle 并不是那么简单 (如果对随机性要求严格)
  - shuffle 算法要准确, unbiased. 常见错误就是 Knuth shuffle 从 [0, n-1] 选 r.
  - 生成随机数时, seed 的选择需要谨慎 
  - 可以用 hardware random-number generator, 然后持续监控随机数生成情况, 免得硬件挂掉了.
    - ?? software 生成随机数的局限是什么?

### sorting application: Convex hull

- convex hull: 包含所有点的周长最小的 fence. (另有一堆等价定义)
- 应用场景
  - 机器人遇到 polygonal obstacle 时的最短绕行路径
  - 寻找 N 个点中距离最大的两个点
- 几何性质: counterclockwise turns; increasing order of polar angle

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1498876854483_image.png)

- Graham scan
  - Choose point p with smallest y-coordinate.  → 排序问题
  - Sort points by polar angle with p.  → 排序问题
  - Consider points in order; discard unless it create a ccw turn.  → 几何计算
- implementing ccw (counterclockwise)
  - 几何计算的 tricky 之处
    - dealing with degenerate cases, 如多点共线的情形
    - coping with floating-point precision
  - 是否为 counterclockwise turn → signed area of triangle → determinant (or cross product)
    - cross product of 2D vectors: $|x\times y| = x_1y_2 - x_2y_1$

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1498877743753_image.png)

  - signed area > 0 => abc is ccw.
  - signed area = 0 => collinear


## w3 Mergesort

### mergesort

- 在一些语言中的应用:
  - Java sort for objects.
  - Perl, C++ stable sort, Python stable sort, Firefox JavaScript, …
- Assertions #Java
  - 在一段代码前后分别放 assertions, 确认条件和结果. 有助于发现逻辑 bug, 也可以指明程序期待的条件和结果是什么, 起到类似文档的作用.
  - java 命令的 `-ea` 和 `-da` 参数可分别启用/禁用 assertions
  - 推荐在开发过程中使用 assertions, 生产环境下禁用.
- auxiliary array
  - auxiliary array 的创建应该在 sort() 递归函数以外, 否则会额外创建很多不必要的数组. → 常见 bug
  - auxiliary array 只创建一次, 这块空间在排序过程中是复用的.
- divide and conquer 思想
- mergesort vs. insertion sort

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1499153682272_image.png)

- in-place merge
  - [in-place algorithm - wikipedia](https://en.wikipedia.org/wiki/In-place_algorithm): 不用 auxiliary data structure (但允许 auxiliary variables 使用少量空间), 主要依靠 element replacement 和 swapping 对 input 做原位更新. 
    - 对 in-place 的要求有不同理解. 严格的 in-place 算法允许的 space complexity 是 constant, 但一般会放宽要求至 O(logN) , 有时甚至允许 O(N) 的额外空间.
  - in-place mergesort 理论上可行, 但算法会非常复杂, 难以使用.
- **practical improvements**
  - use insertion sort for small subarrays
    - mergesort 对于 tiny subarrays 太复杂了, too much overhead (如 recursive calls)
  - stop if already sorted
  - eliminate the copy to the auxiliary array
    - recursive argument switchery

### bottom-up mergesort

- 简单, 无需递归. 但比 top-down mergesort 略慢.
- top-down 和 bottom-up 是 divide-and-conquer 算法的两种方法

### sorting complexity

- computational complexity: 是对某一特定问题下不同算法效率的研究框架, 包括:
  - model of computation
  - cost model
  - upper bound, lower bound, optimal algorithm
- 排序问题的 computational complexity 研究
  - 采用的 model 是 desicion tree.
  - cost model = # compares
  - lower bound: 
    - 不同顺序有 N! 种 → 决策树末端叶子最少有 N! 个 → worst-case 最少要 lg(N!) 次比较. 
    - $\lg(N!) \sim N \lg N$ (Stirling’s approximation)
  - 结论: 就 compare 数量而言, mergesort 是 optimal algorithm
    - 如果 cost model 不是 compare 数量而是 space usage, mergesort 就不是 optimal
    - 如果事先知道以下信息, 以上的 lower bound 也不成立
      - partially ordered arrays
      - duplicate keys  →  3-way quicksort
      - ?? digital properties of keys → radix sort

### comparators

- **comparable interface vs. comparator interface**
  - comparable interface: sort using a type’s natural order. `v.compareTo(w)`
  - comparator interface: sort using an alternative order. `comparator.compare(v, w)`
    - 将数据类型的定义与该类型数据比较的定义解耦
    - Java 的 String 类包含多种 comparator, 如 `String.CASE_INSENSITIVE_ORDER`
- 在 system sort 里使用 comparator
  - 为 Array.sort() 传入第二个参数
- 在自定义 sort 里使用 comparator
  - 如, insertion sort
    public static void sort(Object[] a, Comparator comparator) {
      int N = a.length;
      for (int i = 0; i < N; i++)
        for (int j = 0; j > 0 && less(comparator, a[j], a[j-1]; j--)
          exch(a, j, j-1);
    }
    private static boolean less(Comparator c, Object v, Object w) {
      return c.compare(v, w) < 0;
    }
    private static void exch(Object[] a, int i, int j) {
      Object swap = a[i];
      a[i] = a[j];
      a[j] = swap;
    }      
- comparator 的实现: 可以用 nested class.
  - 一般定义为类的静态成员. (有些情况, 比如比较两个点与第三个点的距离, 这时 comparator 就不是静态成员)
  - 把 Comparator 类的实例定义为 static final 成员: 这样多次排序可以共用一个 Comparator 实例 (而不是每次 `sort(a, new Student.ByName())` )
    public class Student {
      public static final Comparator<Student> BY_NAME = new ByName();
      private final String name;
      private static class ByName implements Comparator<Student> {
        public int compare(Student v, Student w) {
          return v.name.compareTo(w.name);
        }
      }
    }

### stability

- 什么是 stable sort: 使 key 相同的元素保持原有顺序
- #quiz **以下四种 sort 中, 哪些是 stable sort?** selection/selection/shell/merge sort.
  - 排序比较里的 < 和 <= 是有讲究的. mergesort 遇到 key 相同时是从左边还是右边拿, 也是有讲究的. 结论跟具体代码有关系.
- stability 主要跟 reference types 有关. 对 primitive types 基本上不存在 stability 问题.

### assg3 collinear points

- 寻找共线点: 基于排序的方法
  - 对每个点 p, 将剩下所有点按与点 p 连线的斜率排序. 然后看序列里相等斜率的数量.
- 更一般地, 哪些问题可以用排序的思路解决? 
## w3 Quicksort

### quicksort

- 在一些语言中的应用
  - Java sort for primitive types.
  - C qsort, Unix, Visual C++, Python, Matlab, Chrome JavaScript, ...
- 核心思路是 partition. 把一个任意元素放到正确的位置.
  - 经常是提前做一次 shuffle, 然后用第一个元素来 partition
  - partition 的实现是从左和从右分别 scan, 直到两个 pointer 交错.
- 实施细节
  - partitioning in-place 
  - terminating the loop: 对 pointer 交错的判断比想象的更 tricky
  - preserving randomness: 提前 shuffle 一次. 后面每次 partition 后的两部分都仍然是随机的. shuffle 可以避免落入 worst-case.
  - equal keys: 碰到 equal keys 的时候停下来更好?
- performace (num of compares)
  - best case: ~N lgN. 
  - worst case: ~ N^2/2 (shuffle 前提下概率极小)
  - average: ~ 1.39 N lgN
  - 相比 mergesort, 虽然 compare 数量更多, 但每次 compare 的开销更小, less data movement
- Quicksort 特性: in-place; not stable
- practical improvements
  - insertion sort small subarrays
  - median of sample

### ^selection

- Quick-select: 跟 quicksort 很像, 但每一步只需要管一侧的 subarray
  - linear time on average
  - worst case 虽然是 quadratic, 但 shuffle 可保证其概率极小.
- 论文提出了 worst-case 为线性的 compare-based selection algorithm. 但因为线性的系数太大, 实际中未采用.

### duplicate keys

- 重复元素基本不影响 mergesort 的效率, 但会明显降低 quicksort 的效率. 这里问题出在 partition 时把所有相等元素都放到 partition item 的同一侧. 当所有 key 都相等时, 需要 $\sim N^2/2$ 次比较.
- 早期 C 语言里的 qsort 以及很多教科书里, 都有这个问题.
- 解决方法是 3-way partitioning
  - 这种算法是 entropy-optimal. 比较次数 $\sim 2\ln 2 NH$ . (H 为香农熵, $H \leq \lg N$)
  - 当有很多重复元素时, 时间复杂度可以降至线性.

### ^system sorts

- sorting applications
  - obvious aplications
  - 排序后容易解决的问题
    - 找中位数; Identify statistical outliers; 二分查找; 找重复元素
  - non-obvious applications
    - Data compression. Computer graphics. Computational biology. Load balancing on a parallel computer. …
- Java 中的 `Arrays.sort()`
  - 对 primitive types 用 quicksort, 对 objects 用 mergesort
    - 对 objects 来说, mergesort 额外占用的空间相对影响较小. 而 mergesort 的好处是: 1. 稳定, 2. 能保证 n logn 的性能
  - 对每种 primitive type 分别用不同 methods
- 现在广泛使用的 system sort
  - 以 quicksort 为基础
  - 对 small subarray 改用 insertion sort
  - partitioning scheme: 3-way partitioning
  - partitioning item: 因数组规模而异
    - 不用 random shuffle
    - 小数组取中间, 中等数组取 median of 3, 大数组用 median of median of 3x3
    - 相比 random shuffle, 这样 partitioning 效果更好, 开销更小. 但在某些输入下会崩溃.
- 排序算法太多, 选哪个?
  - 结合具体问题的特征来选. 比如: 稳定性,  并行计算, 重复元素, 链表vs数组, 是否随机排列, 是否需要 worst-case 保证, …
- 排序算法小结

![](https://d2mxuefqeaa7sj.cloudfront.net/s_D2C68CF593D0D36085BAFCCA251DDB2986C18E4EA55B34A7A2B129D68A23D323_1500971843744_image.png)

## w4 priority queues

### API and elementary implementations

- priority queue 与 stack, queue, randomized queue 都类似. 区别在于删除什么样的元素.
- priority queue 的主要操作是 insert() 和 delMax() (或 delMin())
- 可以用 array 或 linked list 来实现. 
  - 具体有 lazy 和 eager 两种方法, 或者说 unordered 和 ordered. 
  - lazy 方法 insert 快, delMax 慢; eager 方法相反.
- 而 priority queue 可以使两种操作都是 lgN.
- 例子 - TopM 问题: 从 a stream of N items 中找到最大的 M 个
  - 这里的 N 可能大到读入内存都成问题, 更别说去排序.

### binary heaps

- complete binary tree: 逐层从左到右填满的 binary tree
  - 最大高度 = floor(lg N). (最大高度为 H 意味着层数为 H+1)
- complete binary tree 不需要 explicite links. 可以用 compact array representation.
  - parent of node k: k/2
  - children of node k: 2k, 2k+1
- binary heap = heap-ordered complete binary tree
  - heap-sorted 是一种介于无序与 sorted 之间的状态
- 为什么不使用 pq[0]
  - 主要是算法更简单一些. 另外有时 pq[0] 有其他用途.
- heap algorithm 比较次数
  - insert: 不超过 1+lgN
  - delMax: 不超过 2lgN
- immutability
  - 尽量保证 priority queue 元素为不可变
  - “Classes should be immutable unless there's a very good reason to make them mutable.… If a class cannot be made immutable, you should still limit its mutability as much as possible. ”
  - Java 中使类不可变的方法
    - 成员变量设为 private final
    - 对外部传来的 mutable data 可以存一个副本

### Heapsort

- 两个步骤
  - heap construction: bottom-up 方法
  - sortdown
- 实现时注意下标的起点
- heapsort: 比较次数 < 2NlgN + 2N
- 优点: in-place, N logN worst-case.
- 缺点: 内循环比 quicksort 长; 对 cache memory 利用很差; not stable. 实际中用得较少.

### sorting review (book ch2.5)

- Java 中对 reference 类型, 排序只需要跟 Pointer 打交道. 带来的一个好处是, exchange 的开销比较小, 大体上可认为 exchange 与 compare 的开销相当.
- 当数据有唯一的”自然”顺序时, 可用 Comparable API. 如果需要定义多种顺序, 就用 Comparator API.
- Quicksort 是最快的 general-purpose sort
  - 内循环指令少 (且 cache memory 表现好). running time ~c N lg N, c 比其他 linearithmic sorts 小. 3-way quicksort 对重复 key 较多的情况能达到线性.
- reduction: 一种算法设计技巧. 为解决B问题, 先解决A问题 (常见的 A = sort)
  - 寻找 duplicate/distinct keys
  - the Kendall tau distance between two rankings: 可以用基于 insertion sort 的方法
  - TopM 或 multiway 问题 (对 unbounded input stream): 可基于 priority queue 解决
  - 寻找中位数 或 第 N/c 大的数: 用 quick select 效率高
  - A* algorithm: 把复杂问题 reduce 为一个 priority queue


## Symbol Tables

这部分的学习以教材为主, 对应的课程内容包括:
- w4 elementary symbol tables
- w5 balanced BST
- w5 BST 的几何应用
- w6 hash tables
- w6 symbol table applications

### ch3.1 symbol tables

- symbol table: a data structure for key-value pairs. 
  - two main operations: insert(put), search(get)
- lazy deletion: `put(key, null)`
- ordered symbol table: `public class ST<Key extends Comparable<Key>, Value>`
- searching cost model: 一般用 # compares
- 两种 elementary implementation
  - sequential search in an unordered linked list
  - binary search in an ordered array
    - search 只需要 logN, 但 put 很慢. 构建一个 symbol table 所需的时间仍然是 array size 的平方.

#### equal design :: slides p11

"standard" recipe for user-defined types:

-   optimization for reference equality
-   check against `null`
-   check that two objects are of the same type and cast
-   compare each significant field
    -   if field is a primitive type, use ==
    -   if field is an object, use `equals()`
    -   if field is an array, apply to each entry
        -   `Array.equals(a,b)` or `Arrays.deepEquals(a,b)`

Best practices

-   compare fields mostly likely to differ first
-   make `compareTo()` consistent with `equals()`

#### a symbol-table client     :code:

```java
public class FrequencyCounter {
    public static void main(String[] args) {
        int minlen = Integer.parseInt(args[0]);
        ST<String, Integer> st = new ST<String, Integer>();
        while (!StdIn.isEmpty()) {
            String word = Stdin.readString();
            if (word.length() < minlen) continue;
            if (!st.contains(word)) st.put(word, 1);
            else st.put(word, st.get(word) + 1);
        }

        String max = "";
        st.put(max, 0);
        for (String word: st.keys())
            if (st.get(word) > st.get(max))
                max = word;
        StdOut.println(max + " " + st.get(max));
    }
}
```

#### binary search in an ordered array     :code:

核心是 \`rank\` 方法
完整版本 [BinarySearchST.java](http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BinarySearchST.java.html)

```java
public class BinarySearchST<Key extends Comparable<Key>, Value> {
    private Key[] keys;
    private Values[] vals;
    private int N;

    public BinarySearchST(int capacity) {
        // array-resizing omitted
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
    }

    public int size() { return N; }

    public Value get(Key key) {
        if (isEmpty()) return null;
        int i = rank(key);
        if (i < N && keys[i].compareTo(key) == 0) return vals[i];
        else return null;
    }

    public int rank(Key key) {
        int lo = 0, hi = N-1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }

    public void put(Key key, Value val) {
        int i = rank(key);
        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = val; return;
        }
        for (int j = N; j > i; j--) {
            keys[j] = keys[j-1];
            vals[j] = vals[j-1];
        }
        keys[i] = key; vals[i] = val;
        N++;
    }

    // public void delete(Key key)

    public Key min() { return keys[0]; }
    public Key max() { return keys[N-1]; }
    public Key select(int k) { return vals[k]; }

    public Key ceiling(Key key) {
        int i = rank(key);
        if (i == n) return null;
        else return keys[i];
    }

    public Key floor(Key key) {
        int i = rank(key);
        if (contains(key)) return keys[i];
        else if (i > 0) return keys[i-1];
        else return null;
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Queue<Key> q = new Queue<Key>();
        for (int i = rank(lo); i < rank(hi); i++)
            q.enqueue(keys[i]);
        if (contains(hi))
            q.enqueue(keys[rank(hi)]);
        return q;
    }
}
```

### ch3.2 binary search trees

- BST 只有当 keys 为随机顺序时, insert 和 search 才能达到 lgN. 当 keys 按顺序输入时会是 worst case. 实际中这种情况经常难以避免, 因此需要 balanced BST.
- BST 另一个缺陷是 deletion 无法实现 lgN
- // 分析问题时可以对情况分类, 有助于理顺思路和保证代码完整. ("分类讨论法")
- **basic implementation**
  - put 和 get 逻辑基本相同. 注意 put 需要更新整个路径上的节点.
  - 若是用递归, 递归调用前的代码自上向下, 递归调用后自下而上.
- **analysis**
  - worst case: 按顺序插入
  - 分析 BST 时, 假定 keys 是以随机顺序插入的.
  - **BST vs. quicksort:** 没有重复元素的 quicksort 与 BST 一一应
  - 结论: BST 的 insert 和 search 平均比较次数都是 ~2lnN (1.39lgN)
- **order-based methods and deletion**
  - deletion
    - lazy approach: mark as tombstones. 这种方法用得较少.
    - **Hibbard deletion**
      - 比较 tricky. 思路是用 successor 或 predecessor 代替被删除的节点.
      - 区分三种情况: 待删除节点的 children 数量分别为 0, 1, 2.
        - // 这种分情况分析可以防止代码逻辑漏洞, 也有助于理顺思路
      - deletion 存在性能问题. 在大量混合的随机插入和删除后, 树高期望值为 ~sqrt(N). 即使随机选择 predecessor/successor 也不能解决. (//微小的干扰打破了随机性)
  - 在每个节点存储 size 值: 使 rank() 和 select() 可以高效地实现
  - BST 各种操作所需时间在 worst case 下都和树高成正比
  - 树高
    - L. Devroye: 随机顺序 的 BST 的平均树高当 N 很大时趋近于 3lgN (书 P412)
    - Reed, 2003: 随机插入 N 个不同 keys, 树高期望值为 ~4.311 lnN. (幻灯 P14)
    - worst case 下树高为 N.


#### 代码: BST symbol table     :code:

-   public 方法常返回 Key, 相应 private 方法常返回 Node.
-   booksite 上更完整的版本: [BSTjava](http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/BST.java.html)

```java
public class BST<Key extends Comparable<Key>, Value> {
    private Node root;
    private class Node {
        private Key key;
        private Value val;
        private Node left, right;
        private int N;

        public Node(Key key, Value val, int N) {
            this.key = key; this.val = val; this.N = N;
        }
    }

    public int size() { return size(root); }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x.val;
        else if (cmp < 0) return get(x.left, key);
        else return get(x.right, key);
        }

    public void put(Key key, Value val) {
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) x.val = val;
        else if (cmp < 0) x.left = put(x.left, key, val);
        else x.right = put(x.right, key, val);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Key min() {
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        return min(x.left);
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }

    public Key select(int k) {
        return selelct(root, k).key;
    }

    private Node select(Node x, int k) {
        if (x = null) return null;
        int t = size(x.left);
        if (t > k) return select(x.left, k);
        else if (t < k) return select(x.right, k-t-1);
        else return x;
    }

    public int rank(Key key) {
        return rank(key, root);
    }

    private int rank(Key key, Node x) {
        if (x = null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return rank(key, x.left);
        else if (cmp > 0) return 1 + size(x.left) + rank(key, x.right);
        else return size(x.left);
    }

    public void deleteMin() {
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delelte(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(key lo, key hi) {
        Queue<Key> queue = new Queue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, Queue<Key> queue, key lo, key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }
}
```


### 3.3 balanced search tree


#### 2-3 search trees

-   2-nodes, 3-notes
    -   2-nodes: 即标准 BST 的节点.
    -   3-nodes: 有3个子节点, 2个key.
-   本节提到的 `2-3 tree` 指的是 **perfectly balanced 2-3 search tree**, 所有的 null link 与 root 等距离
-   2-3 tree 的变化过程中会出现临时的 4-nodes (含有3个key). 消灭4-nodes方法是把三个key中间的key提升到父节点, 然后4-nodes变为两个 2-nodes.
    -   "提升中间的 key": 这是保持 balanced 的关键
-   标准BST自上而下生长, 2-3 tree 自下而上生长. root 节点处的临时 4-nodes 消解时, 会使树高 +1. 其余各种操作不改变树高和 perfectly balanced 状态.
-   2-3 tree 虽有很好的特点, 也比较容易理解, 但不宜直接作为实现 symbol table 的数据结构, 因为一来代码逻辑会比较复杂, 二来 overhead 较大.
    -   相对地, 红黑树可以简化代码, 减少 overhead, 但如果不借助 2-3 tree 就不容易理解.


#### 红黑树 Red-black BSTs

-   用一种改进版的 BST 去表达 2-3 tree. red link 犹如 3-nodes 的内部联结.
-   约定为 left-leaning, 与 2-3 tree 一一对应.
-   除了 put 和 delete 以外, 其他方法都和标准 BST 相同.
-   put 里新增了三个 if 语句, 通过 rotate 和 flipColor, 使红黑树总是保持 2-3 tree 的特性.
-   deletion 比较复杂, 有时间再慢慢消化吧.


#### 红黑树的特性

-   树高不超过 2lgN
-   任意节点到 root 的平均距离 ~1.0 lgN
-   红黑树的所有操作都能保证 logarithmic time.


#### 代码: 红黑树 insert

完整版见 booksite: [RedBlackBST.java](http://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/RedBlackBST.java)

```java
public class RedBlackBST<Key extends Comparable<Key>, Value> {

    private Node root;
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        Key key;
        Value val;
        Node left, right;
        int N;
        boolean color;
        Node(Key key, Value val, int N, boolean color) {
            this.key = key; this.val = val; this.N = N; this.color = color;
        }
    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.N = h.N;
        h.N = 1 + size(h.left) + size(h.right);
        return x;
    }

    private Node rotateRight(Node h) {}

    private void flipColors(Node h) {
        h.color = RED;
        h.left.color = BLACK;
        h.right.color = BLACK;
    }

    private int size() {}

    public void put(Key key, Value val) {
        root = put(root, key, val);
        root.color = BLACK;
    }

    private Node put(Node h, Key key, Value val) {
        if (h == null) return new Node(key, val, 1, RED);

        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, val);
        else if (cmp > 0) h.right = put(h.right, key, val);
        else h.val = val;

        // 这三个if语句是红黑树的关键
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);

        h.N = size(h.left) + size(h.right) + 1;
        return h;
    }
}
```


### B-tree

这一节依据课程幻灯而非教材.

-   红黑树的一种推广.
-   适用情形: 数据存储在外部, 分为不同的 page, 相当于节点,
    -   probe: first access to a page. 把 page 读入内存.
    -   access data within a page: 在内存中读数据. 开销远小于 probe.
    -   所以目标是尽量减少 number of probes.
-   每个page最多可存储 M-1 个 key. 存满时拆分为两个 page.
-   例如取 M=1024. 这时即便有数百亿个 key, 需要的 probe 也不超过4次. \(\log_{M-1}N\) ~ \(\log_{M/2}N\)
-   叶节点是 external nodes, 存储 client keys. 其余都是 internal nodes, 保存 keys 的副本, 用于 guide search.


### Geometric applications of BSTs

这一节依据课程内容而非教材


#### 1d range search

ordered symbol table 的扩展. 重要的操作是 range search 和 range count.

1d range count:

    public int size(Key lo, Key hi) {
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

1d range search: find all keys between lo and hi.

-   recursively find all keys in left subtree (if any could fall in range)
-   check key in current node
-   recursively find all keys in right subtree (if any coud fall in range)


#### line segments intersection

**sweep line algorithm**

-   sweep vertical line from left to right
-   遇到水平线段左端点 -> 加入 BST
-   遇到水平线段右端点 -> 移出 BST
    -   BST 里保存的是当前可能发生 intersection 的所有水平线段
-   遇到竖直线段 -> 取两端点y值, 在 BST 里做 range search


#### kd trees

-   2-d orthogonal range search
    -   一种方法是 grid implementation. 适合点分布均匀的情况, 但不能很好地处理 clustering 情况. 而 clustering 实际上挺常见的.
    -   改进: 2d tree. recursively devide space into two halfplanes.
        -   <span class="underline">每个节点对应一个矩形区域</span> (假设整个空间本身是矩形)
        -   pruning rule: if the query rectangle does not intersect the rectangle corresponding to a node, there is no need to explore that node (or its subtrees). A subtree is searched only if it might contain a point contained in the query rectangle.
-   **space partitioning trees:** 一类重要的算法.
    -   自上而下, 节点 key 交错地取为不同维度的坐标 (recursively partition one dimension at a time)
    -   应用例子包括 2d search tree, nearest neighbor search 等
-   nearest neighbor search
    -   pruning rule: if the closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node, there is no need to explore that node (or its subtrees)
    -   每次优先搜索与目标点位于平面分割线同一侧的 subtree. 这样, 运气好的话, 可以剪除另一个 subtree.
    -   typical case: log N. worst case: N.
-   kd tree: 2d tree 扩展到k维. recursively partition k-dimensional space into 2 halfspaces.
-   Appel's algorithm for N-body simulation: 一种 3d-tree 算法


#### interval search trees

-   每个节点代表一个 interval (lo, hi).
-   典型操作: 插入, 查找, 删除, **interval intersection query**.
-   数据结构: BST, 以左端点为 key, 同时在节点上存储整个 subtree 的最大右端点


#### rectangle intersection

-   与 2d orthogonal line segment intersection 问题相似, 都是用 sweep line 算法
-   line segment intersection: sweep line reduce to 1d range search
-   rectangle intersection: sweep line reduce to 1d interval search


### 3.4 hash tables

自测问题:

-   hashing 算法分哪两个步骤
-   对整数/浮点数/字符串 分别采用什么 hash function?
-   Java 中的 `hashCode()` 与这里讨论的 hash function 有何区别?
-   collision resolution 的两种常见方法?
-   linear probing 中 M 与 N 的比值如何影响性能? 一般控制在什么范围?

理解 hash table 的 time-space tradeoff:

-   一个极端是把所有 *可能的* key 当做 index, 构成一个庞大的 array. 只需要一次访问就可以查到.
-   另一个极端是 array 长度等于 key 数量. 查询时用 sequential search.
-   hash table 可以寻找一种中间状态. 而且在 time-space tradeoff 中只需要调整参数, 无需重写代码.

hashing 算法分为两步: 1. hash function, 2. collision resolution

hashing 的局限:

-   performance guarantee 依赖于 hash function 的质量
-   hash function 的计算可能有较大的困难和开销
-   hashing 不能很好地支持 ordered symbol-table 操作


#### hash functions

uniform hashing assumption: hash function 能够均匀且独立地把 keys 分配到整数 0 ~ M-1 (每个 key 分配到不同整数的概率相同). 这个假定是讨论的基础, 尽管实际上没有一个 deterministic function 能严格做到.

-   不同类型数据的 hash function

    -   正整数: 对一个素数取余数. modular hashing.
    -   小数: use modular hashing on the binary representation ??
    -   字符串:
    
        int hash = 0;
        for (int i = 0; i < s.length(); i++)
            hash = (R * hash + s.charAt(i)) % M;
    
    -   compound keys: 如 Date 类型: `int hash = (((day * R + month) % M) * R + year) % M;`

-   hashCode() 方法

    Java 内置数据类型都有 `hashCode()` 方法. `equals` 是 `hashCode()` 相等的充分条件.
    
    自定义 hashCode() 方法
    
```java
public class Transaction {
    private final String who;
    private final Date when;
    private final double amount;

    public int hashCode() {
        int hash = 17;
        hash = 31 * hash + who.hashCode();
        hash = 31 * hash + when.hashCode();
        hash = 31 * hash + ((Double) amount).hashCode();  // primitive type cast 为 wrapper type
        return hash;
    }
}
```
    
-   hash code design (slide p11)
    
    -   "标准"做法:
        -   combine each significant field using the 31x+y rule
        -   if field is primitive type, use wrapper type `hashCode()`
        -   if field is null, return 0
        -   if field is reference type, use `hashCode()`
        -   if field is array, apply to each entry (or, use `Arrays.deepHashCode()`)
    -   basic rule: use the whole key
    
-   hashCode() vs. hash function
    
    -   hashCode() 值域是 32-bit 整数, hash function 值域是 [0, M-1] (作为 array index)
    -   `()x.hashCode() & 0x7fffffff) % M`. 这里的&运算可去除符号.

-   cache the hash

    -   第一次调用 hashCode() 时进行计算, 并存入成员变量 hash
    -   以后调用 hashCode() 时便返回 hash 的值

-   a good hash function

    -   consistent: equal keys must produce the same hash value
    -   efficient to compute
    -   uniformly distribute the set of keys
    
    -   保证均匀性最简单的方法: 使 all the bits of the key 在计算 hash 值时起均等的作用
    -   最常见的错误: ignore significant numbers of the key bits


#### hashing with separate chaining

-   N个key -> M个index -> M个 SequentialSearchST 对象 (unordered linked list)
-   M越大, 占用空间越大, 查找越快. 可在内存允许范围内尽量选较大的M. typical choice: M ~ N/5.
-   每个 list 平均长度是 N/M. search miss 和 insert 需要的比较次数是 ~N/M.

```java
public class SeparateChainingHashST<Key, Value> {
    private int M;
    private SequentialSearchST<Key, Value>[] st;

    public SeparateChainingHashST() { this(997); }
    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[M];
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST();
    }

    private int hash(Key key) { return (key.hashCode() & 0x7fffffff) % M; }
    public Value get(Key key) { return (Value) st[hash(key)].get(key); }
    public void put(Key key, Value val) { st[hash(key)].put(key, val); }
    public Iterable<Key> keys() {}
}
```

#### hashing with linear probing

-   quiz: linear probing 查找过程有哪些状态
-   M > N. alpha = N/M 是 **load factor**. 为了保证性能, 通过 array resizing 使 load factor 保持在 1/8 ~ 1/2.

```java
public class LinearProbingHashST<Key, Value> {
    private int N;
    private int M = 16;
    private Key[] keys;
    private Value[] vals;

    public LinearProbingHashST() {
        keys = (Key[]) new Object[M];
        vals = (Value[]) new Object[M];
    }

    public LinearProbingHashST(int M) {
        this.M = M;
        keys = (Key[]) new Object[M];
        vals = (Value[]) new Object[M];
    }

    private int hash(Key key) { return (key.hashCode() & 0x7fffffff) % M; }

    private void resize(int cap) {
        LinearProbingHashST<Key, Value> t;
        t = new LinearProbingHashST<Key, Value>(cap);
        for (int i = 0; i < M; i++)
            if (keys[i] != null)
                t.put(keys[i], vals[i]);
        keys = t.keys;
        vals = t.vals;
        M = t.M;
    }

    public void put(Key key, Value val) {
        if (N >= M/2) resize(2*M);

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % M)
            if (keys[i].equals(key)) { vals[i] = val; return; }
        keys[i] = key;
        vals[i] = val;
        N++;
    }

    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M)
            if (keys[i].equals(key))
                return vals[i];
        return null;
    }

    public void delete(Key key) {
        // tricky.
    }
}
```

Separate chaining vs. linear probing (slide p40)

-   seperate chaining
    -   easier to implement delete
    -   performance degrades gracefully
    -   clustering less sensitive to poorly-designed hash function
-   linear probing
    -   less wasted space
    -   better cache performance


#### array resizing

-   hashing 算法里的 array resizing 不只是简单地改变数组长度, 还需要对已有的 keys 进行 rehashing.
-   array resizing 对 separate chaining 非必需, 对 linear probing 是必需的


#### hash table vs. 红黑树

hash tables

-   simpler to code
-   对于典型的 key, hash table 的搜索一般比红黑树快很多, 平均时间接近常数.
-   better system support in Java for strings

红黑树

-   performance guarantee 更好
-   高效地实现 ordered operations
-   `compareTo()` 比 `equals()` 和 `hashCode()` 更易实施


### 3.5 applications


#### different symbol-table implementations

主要的两种选择是 hash table 和红黑树. 默认的选择常常是 hash table, 除非有特别的要求需要用红黑树 (如 performance guarantee, ordered operations)

![img](../images/algorithms/algs4_symbol_tables.png)

实现方式的变化

-   当 key 和 value 是 primitive type 时: 前面实现的 hash table 中 key 和 value 都要转成 wrapper type, 会额外增加两个 reference. 当数据量很大, 对性能敏感时, 可以改为直接用 primitive type.


#### set APIs

-   set 就是没有value只有key的 symbol table
-   **SET:** 有序集合
-   **HashSET:** 无序集合
-   应用例子:
    -   dedup: 从 input stream 中识别不重复的元素
    -   whitelist, blacklist: 把白名单/黑名单存入 SET


#### dictionary clients, indexing clients

-   dictionary: 一对一
-   index: 一对多
    -   inverted index: 互换 key 与 value. 一种很常见的情形.

例子: index and inverted index lookup

```java
public class LookupIndex {
    public static void main(String[] args) {
        In in = new In(args[0]);
        String sp = args[1];
        ST<String, Queue<String>> st = new ST<String, Queue<String>>();
        ST<String, Queue<String>> ts = new ST<String, Queue<String>>();

        while (in.hasNextLine()) {
            String[] a = in.readLine().split(sp);
            Stringkey = a[0];
            for (int i = 1; i < a.length; i++) {
                String val = a[i];
                if (!st.contains(key)) st.put(key, new Queue<String>());
                if (!ts.contains(val)) ts.put(val, new Queue<String>());
                st.get(key).enqueue(val);
                ts.get(val).enqueue(key);
            }
        }

        while (!StdIn.isEmpty()) {
            String query = StdIn.readLine();
            if (st.contains(query))
                for (String s: st.get(query))
                    StdOut.println(" " + s);

            if (ts.contains(query))
                for (String s:ts.get(query))
                    StdOut.println(" " + s);
        }
    }
}
```


#### sparse vectors

-   把稀疏矩阵表示为 array of sparse vectors. 比如向量 [0,0,3,0,2] 表示为 {2:3,4:2}
-   sparse vector 只需用大小等于非零元素个数的 HashST 表示.
-   在矩阵与向量乘法 A\*b 中, 假设A的行向量维数是100亿而非零元素只有10个, 那么用 sparse vector 可以使乘法速度提高10亿倍!
-   google page-Rank 用到了类似的原理.

```java
public class SparseVector {
    private HashST<Integer, Double> st;
    public SparseVector() { st = new HashST<Integer, Double>(); }
    public int size() { return st.size(); }
    public void put(int i, double x) { st.put(i, x); }
    public double get(int i) {
        if (!st.contains(i)) return 0.0;
        else return st.get(i);
    }
    public double dot(double[] that) {
        double sum = 0.0;
        for (int i: st.keys())
            sum += that[i] * this.get(i);
        return sum;
    }
}
```

