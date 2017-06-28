package company.facebook;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * 301. Remove the minimum number of invalid parentheses in order to make the input string valid. Return all possible results.

Note: The input string may contain letters other than the parentheses ( and ).

Examples:
"()())()" -> ["()()()", "(())()"]
"(a)())()" -> ["(a)()()", "(a())()"]
")(" -> [""]
 */
public class RemoveInvalidParentheses {
    // https://discuss.leetcode.com/topic/34875/easy-short-concise-and-fast-java-dfs-3-ms-solution/2
    public List<String> removeInvalidParenthesesDFS(String s) {
        List<String> res = new ArrayList<String>();
        helper(s, res, 0, 0, new char[]{'(', ')'});
        return res;
    }
    
    void helper(String s, List<String> res, int last_i, int last_j, char[] pair) {
        for (int i = last_i, stack = 0; i < s.length(); i++) {
            if (s.charAt(i) == pair[0]) {
                stack++;
            }
            if (s.charAt(i) == pair[1]) {
                stack--;
            }
            
            if (stack >= 0) {
                continue;
            }
            
            for (int j = last_j; j <= i; j++) {
                if (s.charAt(j) == pair[1] && (j == last_j || s.charAt(j - 1) != pair[1])) { // Can contain other characters
                    String str = s.substring(0, j) + s.substring(j + 1, s.length());
                    helper(str, res, i, j, pair);
                }
            }
            
            return; // Finished one round, enough
        }
        
        String reversed = new StringBuilder(s).reverse().toString();
        if (pair[0] == '(') { // Only finished '(', need scan in reversed order
            helper(reversed, res, 0, 0, new char[]{')', '('});
        } else {
            res.add(reversed);
        }
    }
    
    // 这个容易理解， BFS
    // https://discuss.leetcode.com/topic/28827/share-my-java-bfs-solution
    public List<String> removeInvalidParentheses(String s) {
        List<String> res = new ArrayList<String>();
        Set<String> visited = new HashSet<String>();
        Queue<String> queue = new LinkedList<String>();
        
        queue.offer(s);
        visited.add(s);
        
        boolean found = false; // Need to find only 1 in 1 length. 
        while (!queue.isEmpty()) {
            String str = queue.poll();
            
            if (isValid(str)) {
                res.add(str);
                found = true;
            }
            
            if (found) { // Once find 1 in a fixed length, dont need to remove more ( or )
                continue;
            }
            
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) != '(' && str.charAt(i) != ')') {
                    continue;
                }
                    
                String reducedStr = str.substring(0, i) + str.substring(i + 1);
                if (!visited.contains(reducedStr)) {
                    queue.offer(reducedStr);
                    visited.add(reducedStr);
                }
            }
        }
        
        return res;
    }
    
    boolean isValid(String s) {
        int count = 0;
        
        for (char c : s.toCharArray()) {
            if (c == '(') {
                count++;
            }
            if (c == ')') {
                count--;
            }
            if (count < 0) {
                return false;
            }
        }
        
        return count == 0;
    }
    
    /*
     * fb问的remove parenthese都是只要一个正确答案，而不是所有，所以走一遍O(n)就行，如果要找出所有，不可能O（n）搞定吧。
     */
    String findOneValid(String s) {
    	int open = 0;
    	int close = 0;
    	StringBuilder sb = new StringBuilder();
    	
    	for (int i = 0; i < s.length(); i++) {
    		char c = s.charAt(i);
    		
    		if (c == '(') {
    			open++;
    			sb.append(c);
    		} else if (c == ')') {
    			if (open > close) {
    				close++;
    				sb.append(c);
    			}
    		} else {
    			sb.append(c);
    		}
    	}
    	
    	// After above, there could be redundant leading (
    	int extra = open - close;
    	
    	for (int i = 0; i < s.length() && extra > 0; i++) {
			if (s.charAt(i) == '(') {
				extra--;
				// if i is the only element, it will throw exception
				if (i + 1 == s.length()) {
					return "";
				}
				sb.deleteCharAt(i);
				
			}
		}
    	
    	return sb.toString();
    }
    
    public static void main(String[] args) {
		RemoveInvalidParentheses ri = new RemoveInvalidParentheses();
		String s1 = "()())()";
		String s2 = "(a)())()";
		String s3 = ")(";
		String res1 = ri.findOneValid(s1);
		String res2 = ri.findOneValid(s2);
		String res3 = ri.findOneValid(s3);
		System.out.println(res1);
		System.out.println(res3);
		System.out.println(res2);
	}
}