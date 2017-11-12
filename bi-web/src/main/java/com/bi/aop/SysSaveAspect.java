package com.bi.aop;

/**
 * 实体类保存，封装操作时间与操作人
 *
 * @author yunmo
 * @email yunmo@bi.com
 * @date 2017年7月7日 上午11:07:35
 */
//@Aspect
//@Component
public class SysSaveAspect {
//	private Logger logger = LoggerFactory.getLogger(getClass());
//
//	@Pointcut("execution(* com.bi.service.impl.*Impl.save(Object+))")
//	public void savePointCut() {
//
//	}
//
//	@Pointcut("execution(* com.bi.service.impl.*Impl.update(Object+))")
//	public void updatePointCut() {
//
//	}
//
//	@Before("savePointCut() || updatePointCut() ")
//	public void saveSysLog(JoinPoint joinPoint) {
//		String className = joinPoint.getTarget().getClass().getName();
//		Object[] args = joinPoint.getArgs();
//
//		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//		String methodName = signature.getName();
//		Method method = null;
//		try {
//			Class<?> clazz = Class.forName(args[0].getClass().getName());
//			if(methodName.equals("save")) {
//				try{
//					method = clazz.getMethod("setCreateBy", Integer.class);
//					method.invoke(args[0], ShiroUtils.getUserId().intValue());
//				} catch (NoSuchMethodException e) {
//					logger.warn(clazz.getName() + "：setCreateBy方法无效");
//				}
//				try{
//					method = clazz.getMethod("setCreateDate", Date.class);
//					method.invoke(args[0], new Date());
//				} catch (NoSuchMethodException e) {
//					logger.warn(clazz.getName() + "：setCreateDate方法无效");
//				}
//			}else if(methodName.equals("update")){
//				try{
//					method = clazz.getMethod("setUpdateDate", Integer.class);
//					method.invoke(args[0], new Date());
//				} catch (NoSuchMethodException e) {
//					logger.warn(clazz.getName() + "：setUpdateDate方法无效");
//				}
//				try{
//					method = clazz.getMethod("setUpdateBy", Date.class);
//					method.invoke(args[0], ShiroUtils.getUserId().intValue());
//				} catch (NoSuchMethodException e) {
//					logger.warn(clazz.getName() + "：setUpdateBy方法无效");
//				}
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
//
//	}
}
